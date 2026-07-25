package com.amanansari.spendly.transaction.state

import com.amanansari.spendly.data.local.dao.AllocatedBudgetPartialDetails
import com.amanansari.spendly.model.ExpIncCategory

sealed interface BudgetModalState {

    data object Hidden : BudgetModalState

    data class ConfirmOverspend(
        val category: ExpIncCategory,
        val overspend: Long,
        val limit: Long
    ) : BudgetModalState

    data class ChooseMoveFrom(
        val overspend: Long,
        val options: List<AllocatedBudgetPartialDetails>,
        val previous: ConfirmOverspend
    ) : BudgetModalState

    data class Success(val message: String) : BudgetModalState
}


