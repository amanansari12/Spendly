package com.amanansari.spendly.data.repository

import android.util.Printer
import com.amanansari.spendly.data.local.dao.AllocatedBudgetPartialDetails
import com.amanansari.spendly.data.local.dao.BudgetAllocationDao
import com.amanansari.spendly.data.local.dao.BudgetDao
import com.amanansari.spendly.data.local.dao.BudgetTotals
import com.amanansari.spendly.data.local.dao.TransactionDao
import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.entity.BudgetAllocationEntity
import com.amanansari.spendly.data.local.entity.BudgetEntity
import com.amanansari.spendly.data.local.entity.TransactionEntity
import com.amanansari.spendly.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val budgetDao: BudgetDao,
    private val budgetAllocationDao: BudgetAllocationDao,
    private val transactionDao: TransactionDao,
    private val userRepository: UserRepository,
    private val budgetAllocationRepository: BudgetAllocationRepository
) {

    //* Top Bar

    fun getUser() : Flow<UserEntity?> = userRepository.getUser()

    //* Balance Summary Card

    fun getBudget(userId : UUID, month: String): Flow<BudgetEntity?> = budgetDao.getBudgetByMonth(userId, month)

    fun getTotalAllocatedAmount(userId : UUID, month: String) : Flow<BudgetTotals>
    = budgetAllocationDao.getTotalAllocatedAmount(userId, month)

    fun getRecentTransactions(userId: UUID): Flow<List<TransactionEntity>> {
        return transactionDao.getRecentTransactions(userId)
    }


    fun getAllocatedBudgetPartialDetail(userId: UUID, monthKey: String) : Flow<List<AllocatedBudgetPartialDetails?>>
            = budgetAllocationRepository.getAllocationsByMonth(userId,monthKey)

}