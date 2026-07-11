package com.amanansari.spendly.data.repository

import com.amanansari.spendly.data.local.dao.BudgetDao
import com.amanansari.spendly.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class BudgetRepository(private val budgetDao: BudgetDao) {

    suspend fun insertBudget(budget : BudgetEntity){
        budgetDao.insertBudget(budget)
    }

    fun getBudgetByMonth(userId: UUID, month : String) : Flow<BudgetEntity?> {
        return budgetDao.getBudgetByMonth(userId, month)
    }

    fun getAllBudget(userId: UUID) : Flow<List<BudgetEntity?>>{
        return budgetDao.getAllBudgets(userId)
    }

}