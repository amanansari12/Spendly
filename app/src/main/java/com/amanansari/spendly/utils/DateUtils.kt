package com.amanansari.spendly.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun monthKeyFrom(epochMillis: Long): String {
    val localDate = Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    return "%04d-%02d".format(localDate.year, localDate.monthValue)
}

fun formatDate(millis: Long?): String {
    return millis?.let {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(it))
    } ?: ""
}

@RequiresApi(Build.VERSION_CODES.O)
private val monthDisplayFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault())

@RequiresApi(Build.VERSION_CODES.O)
fun monthKeyToDisplayLabel(monthKey: String): String =
    YearMonth.parse(monthKey).format(monthDisplayFormatter) // "2026-08" -> "Aug 2026"

fun monthKey(millis: Long): String {
    return SimpleDateFormat("yyyy-MM", Locale.US).format(Date(millis))
}

fun monthYear(millis: Long?): String{
    return millis?.let {
        SimpleDateFormat("MMM yyyy", Locale.getDefault())
            .format(Date(it))
    } ?: "All Dates"
}