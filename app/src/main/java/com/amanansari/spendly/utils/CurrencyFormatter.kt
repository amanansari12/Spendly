package com.amanansari.spendly.utils

import java.text.DecimalFormat

fun formatAmount(amount: String): String {
    if (amount.isBlank()) return ""

    return try {
        val number = amount.toDouble()
        DecimalFormat("#,##0.##").format(number)
    } catch (e: Exception) {
        amount
    }
}