package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "monthly_budget",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"]
        ),

                  ],
    indices = [
        Index(
            value = ["userId", "monthKey"],
            unique = true
        )
    ]
    )
data class BudgetEntity(
    @PrimaryKey
    val monthlyBudgetId : UUID = UUID.randomUUID(), //? We Will be using the UUID for this

    val userId : UUID, //? We Will be using the UUID for this
    val monthKey : String, //? Storing the Month-key YYYY-MM
    val openingBalance : Long, //? This is the leftover money from the previous month.
    val totalIncome : Long, //Sum of income transactions or imported income entries.
    val allocatedAmount : Long, // The total money available to budget.
    val closingBalance: Long, // Shows where default allocations came from.
    val copiedFromMonthKey : String?,
    val isAutoCopied: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt : Long? = null,
    val rowVersion: Int = 1, //? use for sync
)