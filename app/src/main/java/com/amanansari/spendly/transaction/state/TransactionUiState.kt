package com.amanansari.spendly.transaction.state

import com.amanansari.spendly.model.ExpIncCategory

data class TransactionUiState(
    val type : String = "EXPENSE",
    val amount: Long = 0L,
    val categoryId: String = ExpIncCategory.ExpenseCategory.Food.id,
    val note : String = "Transaction",
    val date : Long = System.currentTimeMillis()


)