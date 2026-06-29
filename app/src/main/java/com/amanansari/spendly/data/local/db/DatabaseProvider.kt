package com.amanansari.spendly.data.local.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile //! It Ensures that all the threads get the Updated Database instance/value
    private var INSTANCE : SpendlyDatabase? = null

    fun getDatabase(context : Context) : SpendlyDatabase {

        return INSTANCE ?: synchronized(this){

            val instance = Room.databaseBuilder(
                context.applicationContext,
                SpendlyDatabase::class.java,
                "SpendlyDatabase"
            ).build()

            INSTANCE = instance

            instance
        }

    }
}