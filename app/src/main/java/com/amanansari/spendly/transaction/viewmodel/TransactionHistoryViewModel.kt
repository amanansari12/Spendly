package com.amanansari.spendly.transaction.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.data.repository.UserRepository
import com.amanansari.spendly.transaction.state.TransactionHistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    var selectedMonth by mutableStateOf<String>("")
        private set

    fun updateDate(month : String) {
        this.selectedMonth = month
    }

    var selectedType by mutableStateOf<TransactionType?>(null)
        private set

    fun updateType(type : TransactionType) {
        this.selectedType = type
    }

    var selectedCategory by mutableStateOf<List<String>>(emptyList())
        private set

    fun toggleSelectedCategory(category : String) {
        this.selectedCategory = if(category in selectedCategory){
            selectedCategory.filter { it != category }
        } else {
            selectedCategory + category
        }
    }




    /*
    *
    * private fun fetchCategories() {

        viewModelScope.launch {

            val user = userRepository
                .getUser()
                .firstOrNull()
                ?: return@launch

            val userId = user.userId

            combine(
                selectedMonth,
                selectedType
            ) { month, type ->
                month to type
            }.flatMapLatest { (month, type) ->

                when (type) {

                    TransactionType.EXPENSE ->
                        transactionRepository
                            .getAllocatedBudgetPartialDetail(userId, month)
                            .map { it.filterNotNull() }

                    TransactionType.INCOME ->
                        transactionRepository
                            .getIncomeSourcePartialDetail(userId, month)
                            .map { it.filterNotNull() }
                }

            }.collect { categories ->
                AllocatedCategories = categories
            }
        }
    }
    * */




    val UiState : TransactionHistoryUiState
        get() = TransactionHistoryUiState(
            selectedMonth,
            selectedType,
            selectedCategory,
        )
}