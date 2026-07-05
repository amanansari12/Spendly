package com.amanansari.spendly.onBoarding.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.onBoarding.screen.UserInfoScreen
import com.amanansari.spendly.onBoarding.state.UserInfoUiState
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingStep
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel

@Composable
fun OnboardingRoute(onboardingViewModel: OnboardingViewModel, onNext : () -> Unit){

    when(onboardingViewModel.currentStep){
        OnboardingStep.USER_INFO -> {
            val userUIState = UserInfoUiState(
                name = onboardingViewModel.name,
                email = onboardingViewModel.email,
                currentStep = onboardingViewModel.userInfoStep,
            )

            UserInfoScreen(
                state = userUIState,
                onNameChange = {
                    onboardingViewModel.updateName(it)
                },

                onEmailChange = {
                    onboardingViewModel.updateEmail(it)
                },

                onNext = {
                    onboardingViewModel.goToEmailStep()
                },

                onGetStarted = {
                    onboardingViewModel.completeUserInfoStep()
                }
            )
        }

        OnboardingStep.INITIAL_BALANCE -> {

        }
    }

}