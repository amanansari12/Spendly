package com.amanansari.spendly.onBoarding.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.data.local.preferences.DataStoreManager


import com.amanansari.spendly.data.repository.OnboardingRepository
import com.amanansari.spendly.data.repository.UserRepository
import com.amanansari.spendly.model.CurrencyInfo
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.model.allIncomeCategories
import com.amanansari.spendly.model.categoryFromId
import com.amanansari.spendly.utils.detectDefaultCurrencyInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale
import java.util.UUID
import kotlin.math.roundToLong

enum class UserInfoStep{
    NAME,
    EMAIL
}


sealed class OnboardingCompletionState {
    object Idle : OnboardingCompletionState()
    object Loading : OnboardingCompletionState()
    object Success : OnboardingCompletionState()
    data class Error(val message: String) : OnboardingCompletionState()
}

data class AllocationRow(
    val rowId : String = UUID.randomUUID().toString(),
    val category: ExpIncCategory.ExpenseCategory,
    val amount: Long,
    val amountText : String = "",
    val isCustomised: Boolean = false
)

class OnboardingViewModel(
    private val userRepository: UserRepository,
    private val onboardingRepository: OnboardingRepository,
    private val dataStoreManager: DataStoreManager
    ) : ViewModel() {


    //? Step - 1
    var completionState by mutableStateOf<OnboardingCompletionState>(OnboardingCompletionState.Idle)
        private set

    //? Onboarding Step
    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var userInfoStep by mutableStateOf(UserInfoStep.NAME)
        private set



    fun updateName(name : String){
        this.name = name
    }

    fun updateEmail(email : String){
        this.email = email
    }

    fun goToEmailStep() {
        if (name.isNotBlank()) userInfoStep = UserInfoStep.EMAIL
    }

    var currency : CurrencyInfo by mutableStateOf(detectDefaultCurrencyInfo())
        private set

    fun completeUserInfoStep(): Boolean = email.isNotBlank()



    //? Step - 1 : END


    //? Step - 2
    var initialAmount by mutableLongStateOf(0L)
        private set

    //? This will store the String Value of the amount
    var amountFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    fun updateInitialAmount(newValueText : String){

        this.initialAmount = if(newValueText.isBlank() || newValueText == "."){
            0L
        }
        else{
            BigDecimal(newValueText).movePointRight(2).longValueExact()
        }
        this.amountFieldValue = TextFieldValue(
            text = newValueText,
            selection = TextRange(newValueText.length)
            )
    }

    fun resetInitialBudget() {
        initialAmount = 0L
        amountFieldValue = TextFieldValue("")
    }

    fun completeAddBudgetStep(): Boolean = initialAmount != 0L

    fun completeIncomeSourceStep(): Boolean = true

    val availableIncomeSource : List<ExpIncCategory.IncomeCategory>
        get() = allIncomeCategories

    var selectedIncomeSourceId by mutableStateOf<String>("")
        private set

    val resolvedIncomeCategoryId: String
        get() = if (selectedIncomeSourceId.isNotBlank()) selectedIncomeSourceId else ExpIncCategory.IncomeCategory.Other.id

    fun resetIncomeSelection() {
        selectedIncomeSourceId = ""
    }

    fun toggleIncome(incomeId : String){
        selectedIncomeSourceId = if (incomeId == selectedIncomeSourceId) "" else incomeId
    }

    //?    Step - 2 : END


    //? Step - 3 Budget Allocation
    var allocations by mutableStateOf<List<AllocationRow>>(emptyList())
        private set

    var isCategoryPickerVisible by mutableStateOf(false)
        private set


    val availableCategoriesForPicker : List<ExpIncCategory.ExpenseCategory>
        get() = allExpenseCategories.filterNot { candidate ->
            allocations.any{it.category.id == candidate.id}
        }


    fun removeCategoryFromAllocation(rowId: String) {
        allocations = allocations.filterNot { it.rowId == rowId }
    }

    fun updateAllocationAmount(categoryId: String, newAmountText : String) {
        allocations = allocations.map { row ->

            val newAmount = if (newAmountText.isBlank() || newAmountText == ".") {
                0L
            } else {
                BigDecimal(newAmountText).movePointRight(2).longValueExact()
            }


            if (row.category.id == categoryId) row.copy(amount = newAmount, amountText = newAmountText, isCustomised = true) else row
        }
    }

    fun removeAllocations(){
        allocations = emptyList()
    }

    // Temporary state — only lives while the sheet is open
    var selectedCategoryIds by mutableStateOf<Set<String>>(emptySet())
        private set

    fun openCategoryPicker() {
        selectedCategoryIds = emptySet()   // always start fresh when opening
        isCategoryPickerVisible = true
    }

    fun dismissCategoryPicker() {
        selectedCategoryIds = emptySet()   // discard any half-made selection on cancel
        isCategoryPickerVisible = false
    }

    fun toggleCategorySelection(categoryId: String) {
        selectedCategoryIds = if (categoryId in selectedCategoryIds) {
            selectedCategoryIds - categoryId
        } else {
            selectedCategoryIds + categoryId
        }
    }

    fun confirmCategorySelection() {
        val newRows = availableCategoriesForPicker
            .filter { it.id in selectedCategoryIds }
            .map { category -> AllocationRow(category = category, amount = 0L) }

        allocations = allocations + newRows
        selectedCategoryIds = emptySet()
        isCategoryPickerVisible = false
    }


    //? Step 3 End

    val isOnboardingCompleted = dataStoreManager.onboardingState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    /*TODO: The Main Screen is Fetching Name from this ViewModel which will be Updated.
       The Name will be fetched from the MainScreenViewModel
     */

    val user = userRepository.getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun completeOnboardingStep(){

        viewModelScope.launch {
            if (name.isBlank() || email.isBlank()) {
                completionState = OnboardingCompletionState.Error("Missing user info")
                return@launch
            }

            try{
                completionState = OnboardingCompletionState.Loading

                val user = UserEntity(name = name, email = email)

                onboardingRepository.completeOnboarding(user,
                    initialAmount,
                    allocations,
                    resolvedIncomeCategoryId
                )

                completionState = OnboardingCompletionState.Success
            }
            catch (e: Exception){
                completionState = OnboardingCompletionState.Error(e.message ?: "Failed to complete onboarding")

            }
        }

    }

}