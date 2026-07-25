package com.amanansari.spendly.transaction.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanansari.spendly.data.local.dao.AllocatedBudgetPartialDetails
import com.amanansari.spendly.data.local.entity.BudgetEntity
import com.amanansari.spendly.data.local.entity.TransactionEntity
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.data.repository.TransactionRepository
import com.amanansari.spendly.model.CurrencyInfo
import com.amanansari.spendly.model.categoryFromId
import com.amanansari.spendly.transaction.state.BudgetModalState
import com.amanansari.spendly.transaction.state.TransactionUiState
import com.amanansari.spendly.utils.detectDefaultCurrencyInfo
import com.amanansari.spendly.utils.monthKeyFrom
import com.amanansari.spendly.utils.toCurrencyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject



sealed class TransactionCompletionState {
    object Idle : TransactionCompletionState()
    object Loading : TransactionCompletionState()
    object Success : TransactionCompletionState()
    data class Error(val message: String) : TransactionCompletionState()
}



@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {


    val user = transactionRepository.getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    var completionState by mutableStateOf<TransactionCompletionState>(TransactionCompletionState.Idle)
        private set

    var budgetModalState by mutableStateOf<BudgetModalState>(BudgetModalState.Hidden)
        private set

    //> Transaction Type
    var type by mutableStateOf(TransactionType.EXPENSE)
        private set

    fun updateType(newType : TransactionType){
        this.type = newType
    }


    //> Amount

    var currency : CurrencyInfo by mutableStateOf(detectDefaultCurrencyInfo())
        private set

    var amount by mutableLongStateOf(0L)
        private set
    var amountText by mutableStateOf(TextFieldValue(""))
        private set


    fun updateAmount(newText : String){
        this.amount = if(newText.isBlank() || newText == "."){
            0L
        }
        else{
            BigDecimal(newText).movePointRight(2).longValueExact()
        }

        this.amountText = TextFieldValue(
            text = newText,
            selection = TextRange(newText.length)
        )
    }

    //> CategoryId
    var selectedCategoryId : String by mutableStateOf("")
        private set

    fun toggleCategoryId(categoryId : String?){
        if (categoryId != null) {
            this.selectedCategoryId = if (this.selectedCategoryId == categoryId) {
                ""
            } else {
                categoryId
            }
        }
    }

    //> Note
    var note : String by mutableStateOf("")
        private set

    fun updateNote(newNote : String){
        this.note = newNote
    }

    //> Date of Transaction
    var date : Long by mutableLongStateOf(System.currentTimeMillis())
        private set

    fun updateDate(newDate : Long){
        this.date = newDate
    }


    //>
    //? The following stores all the budget details for the budget allocated by the user for a given month.


    var allocatedBudgets by mutableStateOf<List<AllocatedBudgetPartialDetails>>(emptyList())
        private set

    var currentBudget by mutableStateOf<BudgetEntity?>(null)
        private set

    val unAllocatedFromBudget: Long
        get() = (currentBudget?.totalIncome ?: 0L) - (currentBudget?.allocatedAmount ?: 0L)

    init {
        viewModelScope.launch {
            combine(
            user.filterNotNull(),
            snapshotFlow { date }
        ) { currentUser, currentDate -> currentUser.userId to monthKeyFrom(currentDate) }
            .distinctUntilChanged()
            .flatMapLatest { (userId, monthKey) ->
                combine(
                    transactionRepository
                        .getAllocatedBudgetPartialDetail(userId, monthKey)
                        .map { it.filterNotNull() },

                    transactionRepository
                        .getBudget(userId, monthKey)
                ){ allocatedBudgets, budget ->
                    allocatedBudgets to budget
                }
            }
            .collect { (allocatedBudgetsList, budget) ->
                allocatedBudgets = allocatedBudgetsList
                currentBudget = budget
            }
        }
    }

    fun clearError() {
        if (completionState is TransactionCompletionState.Error) {
            completionState = TransactionCompletionState.Idle
        }
    }


    val uiState : TransactionUiState
        @RequiresApi(Build.VERSION_CODES.O)
        get() = TransactionUiState(
                type = this.type,
                amountText = this.amountText,
                categoryId = this.selectedCategoryId,
                note = this.note,
                date = this.date,
                currency = currency,
                unAllocatedFromBudget = unAllocatedFromBudget,
                errorMessage = when (val state = completionState) {
                    is TransactionCompletionState.Error -> state.message
                    else -> null
                },
            allocatedBudgets = allocatedBudgets
    )

    private fun buildTransactionEntity(userId: UUID) = TransactionEntity(
        userId = userId,
        categoryId = selectedCategoryId,
        type = type,
        currencyCode = currency.code,
        amount = amount,
        occurredAt = date,
        monthKey = monthKeyFrom(date),
        note = note,
    )


    //> STEP 1 — user tapped Save. Check for overspend before actually writing anything.
    @RequiresApi(Build.VERSION_CODES.O)
    fun completeTransaction(){

        viewModelScope.launch {
            if (amount == 0L) {
                completionState = TransactionCompletionState.Error("Please enter an amount")
                return@launch
            }

            if (selectedCategoryId.isBlank()) {
                completionState = TransactionCompletionState.Error("Please select a category")
                return@launch
            }

            if (note.isBlank()) {
                completionState = TransactionCompletionState.Error("Please add a note")
                return@launch
            }

            val currentUser = user.value
            if (currentUser == null) {
                completionState = TransactionCompletionState.Error("User not loaded yet")
                return@launch
            }

            if(type == TransactionType.EXPENSE){
                val budget = allocatedBudgets.find { it.categoryId == selectedCategoryId }
                if(budget != null){
                    val remaining = budget.allocatedAmount - budget.amountSpent
                    val overspend = amount - remaining

                    if(overspend > 0){
                        val category = categoryFromId(selectedCategoryId)
                        if(category != null){
                            budgetModalState = BudgetModalState.ConfirmOverspend(
                                category = category,
                                overspend = overspend,
                                limit = remaining
                            )
                            return@launch
                        }
                    }
                }
            }
            val saved = saveTransaction(currentUser.userId)
            if (saved) {
                budgetModalState = BudgetModalState.Success(
                    message = "Saved. The transaction for ${categoryFromId(selectedCategoryId)?.title} has been saved."
                )
            }
        }
    }

    fun allocatedMoreAndSave(){
        val current = budgetModalState as? BudgetModalState.ConfirmOverspend ?: return

        viewModelScope.launch {
            val currentUser = user.value ?: return@launch


            try {
                transactionRepository.addExpenseWithReallocation(
                    transaction = buildTransactionEntity(currentUser.userId),
                    userId = currentUser.userId,
                    categoryId = selectedCategoryId,
                    monthKey = monthKeyFrom(date),
                    amount = amount,
                    extraAllocation = current.overspend
                )
                completionState = TransactionCompletionState.Success
                val overspend = BigDecimal(current.overspend).movePointLeft(2).toCurrencyString(detectDefaultCurrencyInfo().code)
                budgetModalState = BudgetModalState.Success(
                    "Added $overspend to your ${current.category.title} budget."
                )
            } catch (e: Exception) {
                completionState = TransactionCompletionState.Error(e.message ?: "Couldn't update budget")
            }
        }
    }

    //> STEP 2b — "Move from another category" chosen: show the list first
    fun openMoveFrom() {
        val current = budgetModalState as? BudgetModalState.ConfirmOverspend ?: return
        val options = allocatedBudgets.filter { budget ->
            budget.categoryId != selectedCategoryId &&
                    (budget.allocatedAmount - budget.amountSpent) >= current.overspend
        }
        budgetModalState = BudgetModalState.ChooseMoveFrom(current.overspend, options, previous = current)
    }

    fun goBackFromMoveFrom() {
        val current = budgetModalState as? BudgetModalState.ChooseMoveFrom ?: return
        budgetModalState = current.previous
    }

    //> STEP 2b continued — user picked which category to pull from
    @RequiresApi(Build.VERSION_CODES.O)
    fun moveFromAndSave(fromCategoryId: String) {
        val current = budgetModalState as? BudgetModalState.ChooseMoveFrom ?: return
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            try {
                transactionRepository.addExpenseWithMove(
                    transaction = buildTransactionEntity(currentUser.userId),
                    userId = currentUser.userId,
                    categoryId = selectedCategoryId,
                    monthKey = monthKeyFrom(date),
                    amount = amount,
                    fromCategoryId = fromCategoryId,
                    moveAmount = current.overspend
                )
                completionState = TransactionCompletionState.Success
                val fromName = categoryFromId(fromCategoryId)?.title ?: "that category"

                val overspend = BigDecimal(current.overspend).movePointLeft(2).toCurrencyString(detectDefaultCurrencyInfo().code)
                budgetModalState = BudgetModalState.Success(
                    "Moved $overspend from $fromName into your budget."
                )
            } catch (e: Exception) {
                completionState = TransactionCompletionState.Error(e.message ?: "Couldn't move budget")
            }
        }
    }

    //> STEP 2c — "Log it over budget" chosen
    @RequiresApi(Build.VERSION_CODES.O)
    fun logOverBudgetAndSave() {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            val saved = saveTransaction(currentUser.userId)
            if (saved) {
                budgetModalState = BudgetModalState.Success(
                    "Saved. This category is now over budget for this month."
                )
            }
        }
    }

    //> User tapped outside the sheet or hit "Done"
    fun dismissModal() {
        budgetModalState = BudgetModalState.Hidden
        completionState = TransactionCompletionState.Idle   // breaks the Success+Hidden combo

        // Add Another implies a fresh entry — clear the previous transaction's inputs
        amount = 0L
        amountText = TextFieldValue("")
        note = ""
        selectedCategoryId = ""
    }

    //> Shared save logic, used by every path above
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun saveTransaction(userId: UUID): Boolean {
        return try {
            completionState = TransactionCompletionState.Loading

            val transaction = TransactionEntity(
                userId = userId,
                categoryId = selectedCategoryId,
                type = type,
                currencyCode = currency.code,
                amount = amount,
                occurredAt = date,
                monthKey = monthKeyFrom(date),
                note = note,
            )

            when (type) {
                TransactionType.EXPENSE -> transactionRepository.addExpenseTransaction(
                    transaction, userId, selectedCategoryId, monthKeyFrom(date), amount
                )

                TransactionType.INCOME -> transactionRepository.addIncomeTransaction(
                    transaction, userId, monthKeyFrom(date), amount
                )
            }
            completionState = TransactionCompletionState.Success
            true
        } catch (e: Exception) {
            completionState = TransactionCompletionState.Error(e.message ?: "Transaction Failed")
            false
        }
    }
}