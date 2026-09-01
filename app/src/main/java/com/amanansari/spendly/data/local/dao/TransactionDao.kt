package com.amanansari.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amanansari.spendly.data.local.entity.TransactionEntity
import com.amanansari.spendly.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TransactionDao {
        @Insert(onConflict = OnConflictStrategy.ABORT)
        suspend fun insertTransaction(transaction : TransactionEntity)

        @Update
        suspend fun update(transaction: TransactionEntity)

        // Recent transactions for Home screen preview
        @Query("""
        SELECT * FROM transactions 
        WHERE userId = :userId AND isDeleted = 0
        ORDER BY occurredAt DESC
        LIMIT :limit
        """)
        fun getRecentTransactions(
            userId: UUID,
            limit: Int = 5
        ): Flow<List<TransactionEntity>>

        // Full month list — for a dedicated "All Transactions" screen, not Home
        @Query("""
        SELECT * FROM transactions 
        WHERE userId = :userId AND monthKey = :monthKey AND isDeleted = 0
        ORDER BY occurredAt DESC
        """)
        fun getAllTransactionsForMonth(
            userId: UUID,
            monthKey: String
        ): Flow<List<TransactionEntity>>

        @Query("SELECT * FROM transactions WHERE transactionId = :id AND isDeleted = 0")
        suspend fun getById(id: UUID): TransactionEntity?

        // Soft delete — consistent with your isDeleted convention, not a real DELETE
        @Query("""
        UPDATE transactions 
        SET isDeleted = 1, updatedAt = :updatedAt 
        WHERE transactionId = :id
        """)
        suspend fun softDelete(id: UUID, updatedAt: Long = System.currentTimeMillis())


        @Query("""
                SELECT *
                FROM transactions
                WHERE 
                    (userId = :userId)
                    AND (:category IS NULL OR type = :category)
                    AND (:month IS NULL OR monthKey = :month);
        """)
        fun getAllTransaction(userId : UUID, category : TransactionType?, month : String?) : Flow<List<TransactionEntity?>>

}