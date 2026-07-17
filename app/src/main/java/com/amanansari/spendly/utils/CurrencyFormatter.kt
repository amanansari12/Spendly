package com.amanansari.spendly.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Double.toCurrencyString(currencyCode: String): String {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    format.currency = Currency.getInstance(currencyCode)
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2
    return format.format(this)
}

fun BigDecimal.toCurrencyString(currencyCode: String): String {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    format.currency = Currency.getInstance(currencyCode)
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2
    return format.format(this)
}