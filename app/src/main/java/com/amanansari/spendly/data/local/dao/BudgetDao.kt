package com.amanansari.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.amanansari.spendly.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface BudgetDao {

    @Insert
    suspend fun insertBudget(budget : BudgetEntity)

    @Query(
        """
        UPDATE monthly_budget
        SET totalIncome = totalIncome + :amount,
            closingBalance = closingBalance + :amount,
            updatedAt = :updatedAt,
            rowVersion = rowVersion + 1
        WHERE userId = :userId AND monthKey = :monthKey AND deletedAt IS NULL
    """
    )
    suspend fun addIncome(
        userId: UUID,
        monthKey: String,
        amount: Long,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("SELECT * FROM monthly_budget WHERE userId = :userId AND monthKey = :monthKey AND deletedAt IS NULL LIMIT 1")
    fun getBudgetByMonth(userId: UUID, monthKey : String) : Flow<BudgetEntity?>

    @Query("SELECT * FROM monthly_budget WHERE userId =:userId AND deletedAt IS NULL ORDER BY monthKey DESC")
    fun getAllBudgets(userId: UUID) : Flow<List<BudgetEntity>>


}

//val monthKey = YearMonth.now().toString()
//val monthKey = YearMonth.of(2026, 7).toString()