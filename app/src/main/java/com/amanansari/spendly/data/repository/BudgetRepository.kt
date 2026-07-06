package com.amanansari.spendly.data.repository

import com.amanansari.spendly.data.local.dao.BudgetDao
import com.amanansari.spendly.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val budgetDao: BudgetDao) {

    suspend fun insertBudget(budget : BudgetEntity){
        budgetDao.insertBudget(budget)
    }

    fun getBudgetByMonth(month : String) : Flow<BudgetEntity?> {
        return budgetDao.getBudgetByMonth(month)
    }

    fun getAllBudget() : Flow<List<BudgetEntity?>>{
        return budgetDao.getAllBudgets()
    }

}