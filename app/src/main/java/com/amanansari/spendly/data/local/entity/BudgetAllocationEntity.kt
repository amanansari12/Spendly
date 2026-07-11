package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "budget_allocation",
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["monthlyBudgetId"],
            childColumns = ["monthlyBudgetId"],
            onDelete = ForeignKey.CASCADE   // budget deleted → its allocations go with it
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT  // block deleting a category still in use
        )
    ],
    indices = [
        Index("monthlyBudgetId"),
        Index("categoryId"),
        Index(value = ["userId", "monthKey"])   // supports your "fetch by month" queries directly
    ])
data class BudgetAllocationEntity(
    @PrimaryKey
    val allocationId : UUID = UUID.randomUUID(), //? We Will be using the UUID for this

    val monthlyBudgetId : UUID, //? We Will be using the UUID for this
    val userId : UUID, //? We Will be using the UUID for this
    val monthKey: String,
    val categoryId : String,
    val allocatedAmount : Long,
    val amountSpent : Long,
    val isCustomised : Boolean,
    val createdAt : Long = System.currentTimeMillis(),
    val updatedAt : Long = System.currentTimeMillis(),
    val deletedAt : Long? = null,
    val rowVersion: Int  = 1, //? use for sync
)