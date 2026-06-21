package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey
    val transactionId: String, //? We Will be using the UUID for this

    val userId: Int, //? we will be using the UUID for this
    val incomeSourceId : String, //? we will be using the UUID for this
    val categoryId: String?,
    val type: String,
    val currencyCode : String, //? Supports Future Multi-Currency use
    val note: String?,
//    val occurredAt: Long = System.currentTimeMillis(),// ? Not needed right-now
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val rowVersion: Int?, //? use for sync
    val sourceDeviceId: String?//? use full in sync
)