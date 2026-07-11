package com.amanansari.spendly.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatTransactionTime(occurredAt: Long): String {
    val zdt = Instant.ofEpochMilli(occurredAt).atZone(ZoneId.systemDefault())
    val date = zdt.toLocalDate()
    val today = LocalDate.now()

    return when {
        date == today ->
            zdt.format(DateTimeFormatter.ofPattern("h:mm a"))

        date == today.minusDays(1) ->
            "Yesterday"

        date.isAfter(today.minusDays(7)) ->
            zdt.format(DateTimeFormatter.ofPattern("EEE")) // "Mon", "Tue"...

        date.year == today.year ->
            zdt.format(DateTimeFormatter.ofPattern("d MMM")) // "8 Jul"

        else ->
            zdt.format(DateTimeFormatter.ofPattern("d MMM yyyy")) // "8 Jul 2025"
    }
}