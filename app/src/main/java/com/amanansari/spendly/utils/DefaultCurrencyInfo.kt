package com.amanansari.spendly.utils

import com.amanansari.spendly.onBoarding.viewmodel.CurrencyInfo
import java.util.Currency
import java.util.Locale

fun detectDefaultCurrencyInfo(): CurrencyInfo {
    return try {
        val currency = Currency.getInstance(Locale.getDefault())
        CurrencyInfo(
            code = currency.currencyCode,
            symbol = currency.symbol
        )
    } catch (e: Exception) {
        CurrencyInfo(code = "INR", symbol = "₹") // safe fallback
    }
}