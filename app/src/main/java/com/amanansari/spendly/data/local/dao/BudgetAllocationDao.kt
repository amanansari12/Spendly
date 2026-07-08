package com.amanansari.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amanansari.spendly.data.local.entity.BudgetAllocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetAllocationDao {

    @Insert
    suspend fun insertBudget(budget : BudgetAllocationEntity)

    @Insert
    suspend fun insertAllBudget(budget : List<BudgetAllocationEntity>)

    @Query("SELECT * FROM budget_allocation WHERE monthlyBudgetId = :monthlyBudgetId AND deletedAt IS NULL")
    fun getAllocationsForBudget(monthlyBudgetId: String): Flow<List<BudgetAllocationEntity>>
}
