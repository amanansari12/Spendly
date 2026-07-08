package com.amanansari.spendly.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amanansari.spendly.data.local.converters.UUIDConverters
import com.amanansari.spendly.data.local.dao.BudgetAllocationDao
import com.amanansari.spendly.data.local.dao.CategoryDao
import com.amanansari.spendly.data.local.dao.BudgetDao
import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.entity.BudgetAllocationEntity
import com.amanansari.spendly.data.local.entity.CategoryEntity
import com.amanansari.spendly.data.local.entity.BudgetEntity
import com.amanansari.spendly.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, BudgetEntity::class, CategoryEntity::class, BudgetAllocationEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(UUIDConverters::class)
abstract class SpendlyDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao
    abstract fun monthlyBudgetDao() : BudgetDao
    abstract fun categoryDao() : CategoryDao

    abstract fun budgetAllocationDao() : BudgetAllocationDao
}
