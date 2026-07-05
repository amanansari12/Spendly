package com.amanansari.spendly.data.repository

import com.amanansari.spendly.data.local.dao.MonthlyBudgetDao
import com.amanansari.spendly.data.local.entity.MonthlyBudgetEntity
import com.amanansari.spendly.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class MonthlyBudgetRepository(private val monthlyBudgetDao: MonthlyBudgetDao) {

    suspend fun insertBudget(budget : MonthlyBudgetEntity){
        monthlyBudgetDao.insertBudget(budget)
    }

    fun getBudgetByMonth(month : String) : Flow<MonthlyBudgetEntity?> {
        return monthlyBudgetDao.getBudgetByMonth(month)
    }

    fun getAllBudget() : Flow<List<MonthlyBudgetEntity?>>{
        return monthlyBudgetDao.getAllBudgets()
    }

}