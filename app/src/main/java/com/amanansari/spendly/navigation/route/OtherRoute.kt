package com.amanansari.spendly.navigation.route

import kotlinx.serialization.Serializable


@Serializable
data class AddTransaction(val categoryId : String? = null){
}

@Serializable
object TransactionHistory

@Serializable
object EditBudget