package com.amanansari.spendly.onBoarding.state

import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.onBoarding.viewmodel.AllocationRow
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingCompletionState


data class BudgetAllocationUiState(
    val totalIncome: Double = 0.0,
    val allocations: List<AllocationRow> = emptyList(),
    val availableCategoriesForPicker: List<ExpIncCategory.ExpenseCategory> = emptyList(),
    val isCategoryPickerVisible: Boolean = false,
    val selectedCategoryIds: Set<String> = emptySet(),
) {
    val totalAllocated: Double
        get() = allocations.sumOf { it.amount }

    val remainingToAllocate: Double
        get() = totalIncome - totalAllocated

}