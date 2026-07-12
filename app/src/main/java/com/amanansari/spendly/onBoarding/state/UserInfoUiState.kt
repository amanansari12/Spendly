package com.amanansari.spendly.onBoarding.state

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import com.amanansari.spendly.model.CurrencyInfo
import com.amanansari.spendly.onBoarding.viewmodel.UserInfoStep

data class UserInfoUiState(
    val name: String = "",
    val email: String = "",
    val initialAmount: Double = 0.0,
    val amountFieldValue: TextFieldValue = TextFieldValue(""),
    val currentUserInfoStep: UserInfoStep = UserInfoStep.NAME,
    val currency: CurrencyInfo = CurrencyInfo("INR", "₹")
)