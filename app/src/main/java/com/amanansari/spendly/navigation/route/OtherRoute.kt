package com.amanansari.spendly.navigation.route

import kotlinx.serialization.Serializable


@Serializable
data class AddTransaction(val categoryId : String? = null){
}

@Serializable
object Transactions

@Serializable
object EditBudget