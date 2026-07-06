package com.amanansari.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amanansari.spendly.data.local.entity.BudgetAllocationEntity
import com.amanansari.spendly.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {
    @Insert
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCount(): Int

    @Query("SELECT * FROM budget_allocation WHERE monthlyBudgetId = :monthlyBudgetId AND deletedAt IS NULL")
    fun getAllocationsForBudget(monthlyBudgetId: String): Flow<List<BudgetAllocationEntity>>
}