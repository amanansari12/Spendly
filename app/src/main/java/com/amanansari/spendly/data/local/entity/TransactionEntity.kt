package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE      // user deleted → their transactions go with it
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT     // block deleting a category still referenced by a transaction
        )
    ],
    indices = [
        Index("userId"),
        Index("categoryId"),
        Index(value = ["userId", "occurredAt"]),
        Index(value = ["userId", "monthKey"]),  // supports "transaction history sorted by month" per user
        Index(value = ["userId", "type"])        // supports "show only income" / "show only expenses" filters
    ]
)
data class TransactionEntity(
    @PrimaryKey
    val transactionId: UUID =  UUID.randomUUID(), //? We Will be using the UUID for this

    val userId: UUID, //? we will be using the UUID for this
    val categoryId: String,
    val type: String,
    val currencyCode : String, //? Supports Future Multi-Currency use
    val amount : Long,
    val occurredAt: Long,     // NEW — the actual transaction date, epoch millis. THIS drives all filtering.
    val monthKey : String,  // "2026-07"
    val note: String?,
//    val occurredAt: Long = System.currentTimeMillis(),// ? Not needed right-now
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val rowVersion: Int = 1, //? use for sync
    val sourceDeviceId: String? = null//? use full in sync
)