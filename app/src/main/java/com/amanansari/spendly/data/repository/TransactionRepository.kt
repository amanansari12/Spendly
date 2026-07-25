package com.amanansari.spendly.data.repository

import androidx.room.withTransaction
import com.amanansari.spendly.data.local.dao.AllocatedBudgetPartialDetails
import com.amanansari.spendly.data.local.dao.BudgetAllocationDao
import com.amanansari.spendly.data.local.dao.BudgetDao
import com.amanansari.spendly.data.local.dao.TransactionDao
import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.db.SpendlyDatabase
import com.amanansari.spendly.data.local.entity.BudgetEntity
import com.amanansari.spendly.data.local.entity.TransactionEntity
import com.amanansari.spendly.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val database: SpendlyDatabase,
    private val transactionDao: TransactionDao,
    private val budgetRepository: BudgetRepository,
    private val budgetAllocationDao: BudgetAllocationDao,
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val budgetAllocationRepository: BudgetAllocationRepository
) {

    fun getBudget(userId : UUID, month: String): Flow<BudgetEntity?> = budgetRepository.getBudgetByMonth(userId, month)

    fun getUser(): Flow<UserEntity?> = userRepository.getUser()

    suspend fun addExpenseTransaction(
        transaction : TransactionEntity,
        userId: UUID,
        categoryId: String,
        monthKey: String,
        amount: Long,
        ){

        categoryRepository.ensureSeeded()
        database.withTransaction {
            transactionDao.insertTransaction(transaction)
            budgetAllocationDao.addExpense(userId, categoryId, monthKey, amount)
        }
    }

    suspend fun addExpenseWithReallocation(
        transaction: TransactionEntity, userId: UUID, categoryId: String, monthKey: String,
        amount: Long, extraAllocation: Long,
    ) {
        categoryRepository.ensureSeeded()
        database.withTransaction {
            budgetAllocationDao.adjustAllocatedAmount(userId, monthKey, categoryId, extraAllocation)
            transactionDao.insertTransaction(transaction)
            budgetAllocationDao.addExpense(userId, categoryId, monthKey, amount)
            budgetRepository.recordExtraAllocation(userId, monthKey, extraAllocation)   // ✅ extraAllocation, not amount
        }
    }

    suspend fun addExpenseWithMove(
        transaction: TransactionEntity, userId: UUID, categoryId: String, monthKey: String,
        amount: Long, fromCategoryId: String, moveAmount: Long,
    ) {
        categoryRepository.ensureSeeded()
        database.withTransaction {
            budgetAllocationDao.adjustAllocatedAmount(userId, monthKey, fromCategoryId, -moveAmount)
            budgetAllocationDao.adjustAllocatedAmount(userId, monthKey, categoryId, moveAmount)
            transactionDao.insertTransaction(transaction)
            budgetAllocationDao.addExpense(userId, categoryId, monthKey, amount)
        }
    }

    suspend fun addIncomeTransaction(
        transaction : TransactionEntity,
        userId: UUID,
        monthKey: String,
        amount: Long,
    ){
        categoryRepository.ensureSeeded()
        database.withTransaction {
            transactionDao.insertTransaction(transaction)
            budgetRepository.addIncome(userId,monthKey, amount)
        }
    }



    fun getAllocatedBudgetPartialDetail(userId: UUID, monthKey: String) : Flow<List<AllocatedBudgetPartialDetails?>>
    = budgetAllocationRepository.getAllocationsByMonth(userId,monthKey)


}