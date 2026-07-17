package com.amanansari.spendly.onBoarding.state

import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.onBoarding.viewmodel.AllocationRow
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingCompletionState
import java.math.BigDecimal
import kotlin.math.round
data class BudgetAllocationUiState(
    val totalIncome: Long = 0L,
    val allocations: List<AllocationRow> = emptyList(),
    val availableCategoriesForPicker: List<ExpIncCategory.ExpenseCategory> = emptyList(),
    val isCategoryPickerVisible: Boolean = false,
    val selectedCategoryIds: Set<String> = emptySet(),
) {
    val totalAllocated: BigDecimal
        get() = BigDecimal(allocations.sumOf { it.amount }).movePointLeft(2)

    val remainingToAllocate: BigDecimal
        get() = BigDecimal(totalIncome).movePointLeft(2).subtract(totalAllocated)

}