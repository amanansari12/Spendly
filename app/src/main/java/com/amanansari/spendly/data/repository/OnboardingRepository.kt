package com.amanansari.spendly.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.withTransaction
import com.amanansari.spendly.data.local.dao.BudgetAllocationDao
import com.amanansari.spendly.data.local.dao.CategoryDao
import com.amanansari.spendly.data.local.dao.BudgetDao
import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.db.SpendlyDatabase
import com.amanansari.spendly.data.local.entity.BudgetAllocationEntity
import com.amanansari.spendly.data.local.entity.BudgetEntity
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.data.local.preferences.DataStoreManager
import com.amanansari.spendly.onBoarding.viewmodel.AllocationRow
import java.time.YearMonth


class OnboardingRepository(
    private val database : SpendlyDatabase,
    private val userDao: UserDao,
    private val budgetDao: BudgetDao,
    private val budgetAllocationDao : BudgetAllocationDao,
    private val dataStoreManager: DataStoreManager
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun completeOnboarding(user : UserEntity, initialAmount : Double, allocations : List<AllocationRow>){

        val currentMonthKey = YearMonth.now().toString() // e.g. "2026-07"

        val initialBudget = BudgetEntity(
            userId = user.userId,
            monthKey = currentMonthKey,
            openingBalanceMinor = initialAmount,
            incomeTotalMinor = 0.0,
            allocatedMinor = initialAmount,
            closingBalanceMinor = initialAmount,
            copiedFromMonthKey = null,
            isAutoCopied = false
        )

        val initialBudgetAllocation = allocations.map { row ->
            BudgetAllocationEntity(
                monthlyBudgetId = initialBudget.monthlyBudgetId,
                userId = user.userId,
                monthKey = currentMonthKey,
                categoryId = row.category.id,
                allocatedMinor = row.amount,
                isCustomised = row.isCustomised
            )
        }

        database.withTransaction {
            userDao.insertUser(user)
            budgetDao.insertBudget(initialBudget)
            budgetAllocationDao.insertAllBudget(initialBudgetAllocation)

        }
        dataStoreManager.saveOnboardingState(true)

    }

}