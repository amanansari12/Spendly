package com.amanansari.spendly.onBoarding.state

import com.amanansari.spendly.onBoarding.viewmodel.OnboardingStep
import com.amanansari.spendly.onBoarding.viewmodel.UserInfoStep

data class UserInfoUiState(
    val name: String = "",
    val email: String = "",
    val currentStep: UserInfoStep = UserInfoStep.NAME,
)