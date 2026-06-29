package com.amanansari.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

//? This Entity class represents a table in the database
@Entity(tableName = "users")
data class UserEntity(

    val userId: UUID = UUID.randomUUID(),
    val name: String, //? Column for the user's name
    val email: String, //? Column for the user's email
    val currencyCode: String, //? Column for the user's currency
    val theme: String = "Light", //? Column for the user's theme (default is "Light")
    val createdAt: Long = System.currentTimeMillis(), //? Column for the creation timestamp
    val updatedAt: Long = System.currentTimeMillis() //? Column for to check when was the last update was done
)