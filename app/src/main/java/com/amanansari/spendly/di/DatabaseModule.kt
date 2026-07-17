package com.amanansari.spendly.di

import android.content.Context
import com.amanansari.spendly.data.local.dao.BudgetAllocationDao
import com.amanansari.spendly.data.local.dao.BudgetDao
import com.amanansari.spendly.data.local.dao.CategoryDao
import com.amanansari.spendly.data.local.dao.TransactionDao
import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.db.DatabaseProvider
import com.amanansari.spendly.data.local.db.SpendlyDatabase
import com.amanansari.spendly.data.local.entity.TransactionEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : SpendlyDatabase{
        return DatabaseProvider.getDatabase(context) //? Reusing the Existing Singleton
    }


    @Provides
    fun provideUserDao(database : SpendlyDatabase) : UserDao = database.userDao()

    @Provides
    fun provideCategoryDao(database: SpendlyDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideBudgetDao(database: SpendlyDatabase) : BudgetDao = database.budgetDao()

    @Provides
    fun provideBudgetAllocationDao(database: SpendlyDatabase) : BudgetAllocationDao = database.budgetAllocationDao()

    @Provides
    fun provideTransactionDao(database: SpendlyDatabase) : TransactionDao = database.transactionDao()

}
