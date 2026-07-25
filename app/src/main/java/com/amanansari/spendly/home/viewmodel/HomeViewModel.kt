package com.amanansari.spendly.home.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanansari.spendly.data.repository.HomeRepository
import com.amanansari.spendly.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    val currentMonthKey = YearMonth.now().toString()

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    val uiState : StateFlow<HomeUiState> = homeRepository.getUser()
        .flatMapLatest { user ->
            val userId = user?.userId
            if (userId == null) {
                flowOf(HomeUiState())
            } else {
                combine(
                    homeRepository.getBudget(userId, currentMonthKey),
                    homeRepository.getTotalAllocatedAmount(userId, currentMonthKey),
                    homeRepository.getRecentTransactions(userId)

                ) { budget, allocations, transaction ->

                    HomeUiState(
                        // map these onto whatever fields HomeUiState actually has
                        userName = user.name,
                        defaultCurrency = user.currencyCode,
                        openingBalance = budget?.openingBalance ?: 0L,
                        totalIncome = budget?.totalIncome ?: 0L,
                        totalAllocatedAmount = budget?.allocatedAmount ?: 0L,
                        amountSpentFromAllocated = allocations.totalSpent,
                        closingBalance = budget?.closingBalance ?: 0L,
                        carriedFromMonth = budget?.copiedFromMonthKey ?: "",
                        recentTransaction = transaction
                    )
                }
            }



        }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )




}