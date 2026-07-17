package com.amanansari.spendly.transaction.state

import androidx.compose.ui.text.input.TextFieldValue
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.model.CurrencyInfo
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.transaction.viewmodel.TransactionCompletionState

data class TransactionUiState(
    val type : TransactionType = TransactionType.EXPENSE,
    val amountText: TextFieldValue = TextFieldValue("400.00"),
    val categoryId: String = ExpIncCategory.ExpenseCategory.Food.id,
    val note : String = "Transaction",
    val date : Long = System.currentTimeMillis(),
    val currency: CurrencyInfo = CurrencyInfo("INR", "₹"),
    val errorMessage: String? = null
)