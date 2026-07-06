package com.amanansari.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amanansari.spendly.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert
    suspend fun insertBudget(budget : BudgetEntity)

    @Query("SELECT * FROM monthly_budget WHERE monthKey = :monthKey LIMIT 1")
    fun getBudgetByMonth(monthKey : String) : Flow<BudgetEntity?>

    @Query("SELECT * FROM monthly_budget WHERE deletedAt IS NULL ORDER BY monthKey DESC")
    fun getAllBudgets() : Flow<List<BudgetEntity?>>
}

//val monthKey = YearMonth.now().toString()
//val monthKey = YearMonth.of(2026, 7).toString()