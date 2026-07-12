package com.amanansari.spendly.model

data class CurrencyInfo(
    val code: String,    // "INR" — store this in the DB
    val symbol: String   // "₹"   — use this for display only
)
