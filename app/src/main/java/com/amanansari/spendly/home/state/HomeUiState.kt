package com.amanansari.spendly.home.state

import com.amanansari.spendly.data.local.dao.AllocatedBudgetPartialDetails
import com.amanansari.spendly.data.local.entity.TransactionEntity
import com.amanansari.spendly.utils.detectDefaultCurrencyInfo
import java.math.BigDecimal
import java.math.RoundingMode

data class HomeUiState(
    val userName : String = "User",
    val defaultCurrency : String = detectDefaultCurrencyInfo().code,
    val openingBalance : Long = 0L,
    val totalIncome : Long = 0L,
    val totalAllocatedAmount : Long = 0L,
    val amountSpentFromAllocated : Long = 0L,
    val closingBalance : Long = 0L,
    val carriedFromMonth : String = "",
    val recentTransaction : List<TransactionEntity> = emptyList(),
    val budgetPartialDetail : List<AllocatedBudgetPartialDetails?> = emptyList()
){
    val budgetUsedPercentage: BigDecimal
        get() = if (totalAllocatedAmount > 0L) {
            BigDecimal(amountSpentFromAllocated)
                .divide(BigDecimal(totalAllocatedAmount), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal(100))
                .coerceIn(BigDecimal.ZERO, BigDecimal(100))
        } else BigDecimal.ZERO


    val isUnAllocatedAmountLeft : Boolean
        get() = (openingBalance+totalIncome - totalAllocatedAmount) > 0

    val isTransaction : Boolean
        get() = recentTransaction.isNotEmpty()
}