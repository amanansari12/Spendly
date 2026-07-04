package com.amanansari.spendly.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amanansari.spendly.data.local.converters.UUIDConverters
import com.amanansari.spendly.data.local.dao.MonthlyBudgetDao
import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.entity.MonthlyBudgetEntity
import com.amanansari.spendly.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, MonthlyBudgetEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(UUIDConverters::class)
abstract class SpendlyDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao
    abstract fun monthlyBudgetDao() : MonthlyBudgetDao
}
