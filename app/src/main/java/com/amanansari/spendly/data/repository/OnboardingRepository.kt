package com.amanansari.spendly.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.amanansari.spendly.data.local.dao.MonthlyBudgetDao
import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.db.SpendlyDatabase
import com.amanansari.spendly.data.local.entity.MonthlyBudgetEntity
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.data.local.preferences.DataStoreManager
import java.time.YearMonth


class OnboardingRepository(
    private val database : SpendlyDatabase,
    private val userDao: UserDao,
    private val monthlyBudgetDao: MonthlyBudgetDao,
    private val dataStoreManager: DataStoreManager
) {



    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun completeOnboarding(user : UserEntity, initialAmount : Double){

        val currentMonthKey = YearMonth.now().toString() // e.g. "2026-07"

        val initialBudget = MonthlyBudgetEntity(
            userId = user.userId,
            monthKey = currentMonthKey,
            openingBalanceMinor = initialAmount,
            incomeTotalMinor = 0.0,
            allocatedMinor = initialAmount,
            closingBalanceMinor = initialAmount,
            copiedFromMonthKey = null,
            isAutoCopied = false
        )

        database.withTransaction {
            userDao.insertUser(user)
            monthlyBudgetDao.insertBudget(initialBudget)
            dataStoreManager.saveOnboardingState(true)
        }

    }
}