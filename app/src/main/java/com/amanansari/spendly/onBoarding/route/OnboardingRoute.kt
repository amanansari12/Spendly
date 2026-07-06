package com.amanansari.spendly.onBoarding.route

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.onBoarding.screen.InitialBudgetScreen
import com.amanansari.spendly.onBoarding.screen.UserInfoScreen
import com.amanansari.spendly.onBoarding.state.UserInfoUiState
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingStep
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnboardingRoute(onboardingViewModel: OnboardingViewModel, onNext : () -> Unit){
    val userUIState = UserInfoUiState(
        name = onboardingViewModel.name,
        email = onboardingViewModel.email,
        initialAmount = onboardingViewModel.initialAmount,
        currentStep = onboardingViewModel.userInfoStep,
        )

    when(onboardingViewModel.currentStep){
        OnboardingStep.USER_INFO -> {


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

                onNextStep = {
                    onboardingViewModel.completeUserInfoStep()
                }
            )
        }

        OnboardingStep.INITIAL_BALANCE -> {
            InitialBudgetScreen(
                state = userUIState,
                onAmountChange = {
                    onboardingViewModel.updateInitialAmount(it)
                },
                onNextStep = {
                    onboardingViewModel.completeOnboardingStep()
                },
            )
        }
    }

}