package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


//? This Entity class represents a table in the database
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //? Primary key with auto-generation
    val name: String, //? Column for the user's name
    val currency: String, //? Column for the user's currency
    val theme: String = "Light", //? Column for the user's theme (default is "Light")
    val createdAt: Long = System.currentTimeMillis() //? Column for the creation timestamp
)

