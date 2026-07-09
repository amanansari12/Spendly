package com.amanansari.spendly.utils

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