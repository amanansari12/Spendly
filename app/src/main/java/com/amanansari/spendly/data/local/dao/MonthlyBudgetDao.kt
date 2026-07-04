package com.amanansari.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amanansari.spendly.data.local.entity.MonthlyBudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyBudgetDao {

    @Insert
    suspend fun insertBudget(budget : MonthlyBudgetEntity)

    @Query("SELECT * FROM monthly_budget WHERE monthKey = :monthKey LIMIT 1")
    fun getBudgetByMonth(monthKey : String) : Flow<MonthlyBudgetEntity?>

    @Query("SELECT * FROM monthly_budget WHERE deletedAt IS NULL ORDER BY monthKey DESC")
    fun getAllBudgets() : Flow<List<MonthlyBudgetEntity?>>
}

//val monthKey = YearMonth.now().toString()
//val monthKey = YearMonth.of(2026, 7).toString()