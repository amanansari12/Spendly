package com.amanansari.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amanansari.spendly.data.local.entity.BudgetAllocationEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID


data class BudgetTotals(
    val totalSpent: Long,
    val totalAllocated: Long
)
@Dao
interface BudgetAllocationDao {

    @Insert
    suspend fun insertBudget(budget : BudgetAllocationEntity)

    @Insert
    suspend fun insertAllBudget(budget : List<BudgetAllocationEntity>)

    @Query("SELECT * FROM budget_allocation WHERE userId = :userId AND monthlyBudgetId = :monthKey AND deletedAt IS NULL")
    fun getAllocationsForBudgetByMonth(userId : UUID, monthKey: String): Flow<List<BudgetAllocationEntity>>

    @Query("""
        SELECT COALESCE(SUM(amountSpent), 0) AS totalSpent, 
           COALESCE(SUM(allocatedAmount), 0) AS totalAllocated
        FROM budget_allocation
        WHERE userId = :userId
        AND monthKey = :monthKey
        AND deletedAt IS NULL
            """
    )
    fun getTotalAllocatedAmount(
        userId: UUID,
        monthKey: String
    ): Flow<BudgetTotals>
}
