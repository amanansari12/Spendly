package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_allocation")
data class BudgetAllocationEntity(
    @PrimaryKey
    val allocationId : String, //? We Will be using the UUID for this

    val monthlyBudgetId : String, //? We Will be using the UUID for this
    val userId : String, //? We Will be using the UUID for this
    val categoryId : String,
    val allocatedMinor : Long,
    val isCustomised : Boolean,
    val createdAt : Long = System.currentTimeMillis(),
    val updatedAt : Long = System.currentTimeMillis(),
    val deletedAt : Long? = null,
    val rowVersion: Int?, //? use for sync
)