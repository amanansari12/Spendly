package com.amanansari.spendly.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.withTransaction
import com.amanansari.spendly.data.local.dao.BudgetAllocationDao
import com.amanansari.spendly.data.local.dao.CategoryDao
import com.amanansari.spendly.data.local.dao.BudgetDao
import com.amanansari.spendly.data.local.dao.TransactionDao
import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.db.SpendlyDatabase
import com.amanansari.spendly.data.local.entity.BudgetAllocationEntity
import com.amanansari.spendly.data.local.entity.BudgetEntity
import com.amanansari.spendly.data.local.entity.TransactionEntity
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.data.local.preferences.DataStoreManager
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.categoryFromId
import com.amanansari.spendly.onBoarding.viewmodel.AllocationRow
import com.amanansari.spendly.utils.detectDefaultCurrencyInfo
import java.time.YearMonth
import kotlin.math.roundToLong


class OnboardingRepository(
    private val database : SpendlyDatabase,
    private val userDao: UserDao,
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao,
    private val budgetAllocationDao : BudgetAllocationDao,
    private val dataStoreManager: DataStoreManager
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun completeOnboarding(user : UserEntity,
                                   initialAmount : Long,
                                   allocations : List<AllocationRow>,
                                   incomeSourceId : String
    ){

        val currentMonthKey = YearMonth.now().toString() // e.g. "2026-07"
        val totalAllocated = allocations.sumOf { it.amount.times(100).roundToLong() }
        val initialBudget = BudgetEntity(
            userId = user.userId,
            monthKey = currentMonthKey,
            openingBalance = 0L,
            totalIncome = initialAmount,
            allocatedAmount = initialAmount,
            closingBalance = initialAmount - totalAllocated,
            copiedFromMonthKey = null,
            isAutoCopied = false
        )

        val initialBudgetAllocation = allocations.map { row ->
            BudgetAllocationEntity(
                monthlyBudgetId = initialBudget.monthlyBudgetId,
                userId = user.userId,
                monthKey = currentMonthKey,
                categoryId = row.category.id,
                allocatedAmount = row.amount.times(100).roundToLong(),
                isCustomised = row.isCustomised
            )
        }

        val transaction = TransactionEntity(
            userId = user.userId,
            categoryId = incomeSourceId,
            type = "INCOME",
            currencyCode = detectDefaultCurrencyInfo().code,
            amount = initialAmount,
            occurredAt = System.currentTimeMillis(),
            monthKey = currentMonthKey,
            note = "Initial Balance",

        )

        database.withTransaction {
            userDao.insertUser(user)
            budgetDao.insertBudget(initialBudget)
            transactionDao.insertTransaction(transaction)
            budgetAllocationDao.insertAllBudget(initialBudgetAllocation)

        }
        dataStoreManager.saveOnboardingState(true)

    }

}