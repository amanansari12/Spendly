package com.amanansari.spendly.data.repository

import com.amanansari.spendly.data.local.dao.AllocatedBudgetPartialDetails
import com.amanansari.spendly.data.local.dao.BudgetAllocationDao
import com.amanansari.spendly.data.local.entity.BudgetAllocationEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BudgetAllocationRepository @Inject constructor(
    private val budgetAllocationDao: BudgetAllocationDao
) {

    fun getAllocationsByMonth(userId: UUID, monthKey: String): Flow<List<AllocatedBudgetPartialDetails?>>
        = budgetAllocationDao.getAllocationsByMonth(userId, monthKey)




}