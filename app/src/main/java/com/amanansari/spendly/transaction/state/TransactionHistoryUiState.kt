package com.amanansari.spendly.transaction.state

import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.utils.monthKey

data class TransactionHistoryUiState(
    val selectedMonth: String = monthKey(System.currentTimeMillis()) ,
    val selectedType: TransactionType? = TransactionType.EXPENSE,
    val selectedCategory: List<String> = emptyList()
)
