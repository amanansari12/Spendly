package com.amanansari.spendly.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
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