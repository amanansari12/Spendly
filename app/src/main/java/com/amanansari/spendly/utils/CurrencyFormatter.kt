package com.amanansari.spendly.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatAmount(amount: String): String {
    val cleaned = amount.replace(",", "").trim()
    if (cleaned.isEmpty()) return ""

    // allow up to 12 digits before decimal and up to 2 after decimal
    val regex = Regex("^\\d{1,12}(\\.\\d{0,2})?$|^\\.\\d{1,2}$")

    if (!regex.matches(cleaned)) return amount

    return try {
        val number = BigDecimal(cleaned)
        val formatter = DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.US))
        formatter.format(number)
    } catch (e: Exception) {
        amount
    }
}