package com.amanansari.spendly.onBoarding.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allExpenseCategories
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

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
    val amount: Double,
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

    fun completeUserInfoStep(): Boolean = email.isNotBlank()

    //? Step - 2
    var initialAmount by mutableDoubleStateOf(0.0)
        private set

    //? This will store the String Value of the amount
    var amountFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    fun updateInitialAmount(amount : Double, newValueText : String){
        this.initialAmount = amount
        this.amountFieldValue = TextFieldValue(
            text = newValueText,
            selection = TextRange(newValueText.length)
            )
    }

    fun resetInitialBudget() {
        initialAmount = 0.0
        amountFieldValue = TextFieldValue("")
    }

    fun completeAddBudgetStep(): Boolean = initialAmount != 0.0


    //? Step - 3 Budget Allocation
    var allocations by mutableStateOf<List<AllocationRow>>(emptyList())
        private set

    var isCategoryPickerVisible by mutableStateOf(false)
        private set


    val availableCategoriesForPicker : List<ExpIncCategory.ExpenseCategory>
        get() = allExpenseCategories.filterNot { candidate ->
            allocations.any{it.category.id == candidate.id}
        }

//    fun addCategoryToAllocation(category: ExpIncCategory.ExpenseCategory) {
//        allocations = allocations + AllocationRow(category = category, amount = 0.0)
//        isCategoryPickerVisible = false
//    }

    fun removeCategoryFromAllocation(rowId: String) {
        allocations = allocations.filterNot { it.rowId == rowId }
    }

    fun updateAllocationAmount(categoryId: String, newAmount: Double, newAmountText : String) {
        allocations = allocations.map { row ->
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
            .map { category -> AllocationRow(category = category, amount = 0.0) }

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
                onboardingRepository.completeOnboarding(user, initialAmount, allocations)
                completionState = OnboardingCompletionState.Success
            }
            catch (e: Exception){
                completionState = OnboardingCompletionState.Error(e.message ?: "Failed to complete onboarding")

            }
        }

    }

}