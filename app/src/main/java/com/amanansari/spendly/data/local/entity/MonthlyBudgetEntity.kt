package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_budget")
data class MonthlyBudgetEntity(
    @PrimaryKey
    val monthlyBudgetId : String, //? We Will be using the UUID for this

    val userId : String, //? We Will be using the UUID for this
    val monthKey : String, //? Storing the Month-key YYYY-MM
    val openingBalanceMinor : Long, //? This is the leftover money from the previous month.
    val incomeTotalMinor : Long, //Sum of income transactions or imported income entries.
    val allocatedMinor : Long, // The total money available to budget.
    val closingBalanceMinor: Long, // Shows where default allocations came from.
    val copiedFromMonthKey : Long,
    val isAutoCopied: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt : Long = System.currentTimeMillis(),
    val rowVersion: Int?, //? use for sync
)