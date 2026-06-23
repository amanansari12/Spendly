package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoriesEntity(
    @PrimaryKey
    val categoryId : String, // matches ExpIncCategory.id, e.g. "food"
    val name: String,
    val type: String, // "EXPENSE" or "INCOME"
    val isSystem: Boolean = true,         // built-in vs user-created later
    val isActive: Boolean = true,
    val sortOrder: Int = 0
)