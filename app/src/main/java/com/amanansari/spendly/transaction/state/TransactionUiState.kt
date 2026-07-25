package com.amanansari.spendly.transaction.state

import android.health.connect.datatypes.units.Percentage
import androidx.compose.ui.text.input.TextFieldValue
import com.amanansari.spendly.data.local.dao.AllocatedBudgetPartialDetails
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.model.CurrencyInfo
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.utils.detectDefaultCurrencyInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

data class TransactionUiState(
    val type : TransactionType = TransactionType.EXPENSE,
    val amountText: TextFieldValue = TextFieldValue("400.00"),
    val defaultCurrency: CurrencyInfo = detectDefaultCurrencyInfo(),
    val categoryId: String = ExpIncCategory.ExpenseCategory.Food.id,
    val note : String = "Transaction",
    val date : Long = System.currentTimeMillis(),
    val currency: CurrencyInfo = CurrencyInfo("INR", "₹"),
    val unAllocatedFromBudget : Long = 0L,
    val errorMessage: String? = null,
    val allocatedBudgets : List<AllocatedBudgetPartialDetails> = listOf(
        AllocatedBudgetPartialDetails(
            categoryId = ExpIncCategory.ExpenseCategory.Food.id,
            allocatedAmount = 500000L,   // ₹5,000.00 in paise
            monthKey = "2026-07",
            userId = UUID.randomUUID(),
            amountSpent = 70000L
        ),
        AllocatedBudgetPartialDetails(
            categoryId = ExpIncCategory.ExpenseCategory.Rent.id,
            allocatedAmount = 1500000L, // ₹15,000.00 in paise
            monthKey = "2026-07",
            userId = UUID.randomUUID(),
            amountSpent = 70000L
        )
    )
){

    val allocatedAmountToCategory = allocatedBudgets.find { it.categoryId == categoryId }?.allocatedAmount ?: 0L

    val amountSpentToCategory = allocatedBudgets.find { it.categoryId == categoryId }?.amountSpent ?: 0L

    val budgetSpentPercentage : BigDecimal
        get() = run {
            if (allocatedAmountToCategory > 0) {
                BigDecimal(amountSpentToCategory)
                    .divide(
                        BigDecimal(allocatedAmountToCategory),
                        4,
                        RoundingMode.HALF_UP
                    )
            } else {
                BigDecimal.ZERO
            }
        }
}