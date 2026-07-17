package com.amanansari.spendly.transaction.viewmodel

import android.R.attr.name
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanansari.spendly.data.local.entity.TransactionEntity
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.data.repository.TransactionRepository
import com.amanansari.spendly.model.CurrencyInfo
import com.amanansari.spendly.transaction.state.TransactionUiState
import com.amanansari.spendly.utils.detectDefaultCurrencyInfo
import com.amanansari.spendly.utils.monthKeyFrom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty


sealed class TransactionCompletionState {
    object Idle : TransactionCompletionState()
    object Loading : TransactionCompletionState()
    object Success : TransactionCompletionState()
    data class Error(val message: String) : TransactionCompletionState()
}


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



    val uiState : TransactionUiState
        get() = TransactionUiState(
                type = this.type,
                amountText = this.amountText,
                categoryId = this.selectedCategoryId,
                note = this.note,
                date = this.date,
                currency = currency,
                errorMessage = when (val state = completionState) {
                    is TransactionCompletionState.Error -> state.message
                    else -> null
                }
    )


    //> Last Step
    @RequiresApi(Build.VERSION_CODES.O)
    fun completeTransaction(){

        viewModelScope.launch {
            if(amount == 0L || selectedCategoryId.isBlank()){
                completionState = TransactionCompletionState.Error("Missing Amount")
                return@launch
            }

            val currentUser = user.value
            if (currentUser == null) {
                completionState = TransactionCompletionState.Error("User not loaded yet")
                return@launch
            }


            try{
                completionState = TransactionCompletionState.Loading

                val transaction = TransactionEntity(
                    userId = currentUser.userId,
                    categoryId = selectedCategoryId,
                    type = type,
                    currencyCode = currency.code,
                    amount = amount,
                    occurredAt = date,
                    monthKey = monthKeyFrom(date),
                    note = note,

                )

                when(type){

                    TransactionType.EXPENSE -> {
                        transactionRepository.addExpenseTransaction(
                            transaction,
                            currentUser.userId,selectedCategoryId,
                            monthKeyFrom(date),
                            amount
                        )
                    }

                    TransactionType.INCOME -> {
                        transactionRepository.addIncomeTransaction(
                            transaction,
                            currentUser.userId,
                            monthKeyFrom(date),
                            amount
                        )
                    }
                }
                completionState = TransactionCompletionState.Success
            }
            catch (e: Exception){
                completionState = TransactionCompletionState.Error(e.message ?: "Transaction Failed")
            }
        }


    }

}