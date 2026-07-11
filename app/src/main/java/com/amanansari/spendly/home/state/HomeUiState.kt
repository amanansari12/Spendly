package com.amanansari.spendly.home.state

import com.amanansari.spendly.data.local.entity.TransactionEntity

data class HomeUiState(
    val userName : String = "User",
    val defaultCurrency : String = "INR",
    val openingBalance : Long = 0L,
    val totalIncome : Long = 0L,
    val totalAllocatedAmount : Long = 0L,
    val amountSpentFromAllocated : Long = 0L,
    val closingBalance : Long = 0L,
    val carriedFromMonth : String = "",
    val recentTransaction : List<TransactionEntity> = emptyList()
){
    val budgetUsedPercentage: Float
        get() = if (totalAllocatedAmount > 0) {
            (amountSpentFromAllocated.toFloat() / totalAllocatedAmount.toFloat() * 100).coerceIn(0f, 100f)
        } else 0f

    val isUnAllocatedAmountLeft : Boolean
        get() = (openingBalance+totalIncome - totalAllocatedAmount) > 0

    val isTransaction : Boolean
        get() = recentTransaction.isNotEmpty()
}