package com.amanansari.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amanansari.spendly.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(txn : TransactionEntity)

    @Query("SELECT * FROM transactions")
    fun getAllTransaction() : Flow<List<TransactionEntity>>

}