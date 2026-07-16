package com.amanansari.spendly.data.repository

import androidx.room.withTransaction
import com.amanansari.spendly.data.local.dao.BudgetAllocationDao
import com.amanansari.spendly.data.local.dao.BudgetDao
import com.amanansari.spendly.data.local.dao.TransactionDao
import com.amanansari.spendly.data.local.db.SpendlyDatabase
import com.amanansari.spendly.data.local.entity.TransactionEntity
import java.util.UUID
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val database: SpendlyDatabase,
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val budgetAllocationDao: BudgetAllocationDao
) {

    suspend fun addExpenseTransaction(
        transaction : TransactionEntity,
        userId: UUID,
        categoryId: String,
        monthKey: String,
        amount: Long,
        ){

        database.withTransaction {
            transactionDao.insertTransaction(transaction)
            budgetAllocationDao.addExpense(userId, categoryId, monthKey, amount)
        }
    }


    suspend fun addTransaction(transaction : TransactionEntity){
        transactionDao.insertTransaction(transaction)
    }

    suspend fun addIncome(userId : UUID, amount : Long, monthKey : String) {
        budgetDao.addIncome(userId,monthKey,amount)
    }

    suspend fun addExpense(
        userId: UUID,
        categoryId: String,
        monthKey: String,
        amount: Long,
    ){
        budgetAllocationDao.addExpense(userId, categoryId, monthKey, amount)
    }

}