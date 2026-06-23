package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(

    @PrimaryKey
    val settingsId: String, //? We Will be using the UUID for this

    val userId: String, //? We Will be using the UUID for this
    val defaultCurrencyCode: String = "INR",
    val carryForwardEnabled: Boolean = true,
    val autoCopyPreviousBudget: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)