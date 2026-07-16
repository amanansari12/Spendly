package com.amanansari.spendly.transaction.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amanansari.spendly.data.repository.TransactionRepository
import com.amanansari.spendly.transaction.state.TransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    //> Transaction Type
    var type : String by mutableStateOf("")
        private set

    //> Amount
    var amount by mutableLongStateOf(0L)
        private set
    var amountText by mutableStateOf("")
        private set

    fun updateAmount(newText : String){
        this.amount = if(newText == "" || newText == "."){
            0L
        }
        else{
            BigDecimal(newText).movePointRight(2).longValueExact()
        }

        this.amountText = newText
    }

    //> CategoryId
    var categoryId : String by mutableStateOf("")
        private set

    fun updateCategoryId(categoryId : String){
        this.categoryId = categoryId
    }

    //> Note
    var note : String by mutableStateOf("")
        private set

    fun updateNote(newNote : String){
        this.note = newNote
    }

    //> Date of Transaction
    var date : Long by mutableLongStateOf(0L)
        private set

    fun updateDate(newDate : Long){
        this.date = newDate
    }

    val uiState : TransactionUiState = TransactionUiState(
                type = this.type,
                amount = this.amount,
                categoryId = this.categoryId,
                note = this.note,
                date = this.date
    )

}