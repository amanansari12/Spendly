package com.amanansari.spendly.navigation.graph

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amanansari.spendly.navigation.route.InitialBudget
import com.amanansari.spendly.navigation.route.InitialBudgetAllocation
import com.amanansari.spendly.navigation.route.UserInfo
import com.amanansari.spendly.onBoarding.screen.InitialBudgetAllocationScreen
import com.amanansari.spendly.onBoarding.screen.InitialBudgetScreen
import com.amanansari.spendly.onBoarding.screen.UserInfoScreen
import com.amanansari.spendly.onBoarding.state.BudgetAllocationUiState
import com.amanansari.spendly.onBoarding.state.UserInfoUiState
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnboardingNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onboardingViewModel: OnboardingViewModel,

){

    NavHost(
        navController = navController,
        startDestination = UserInfo,
        modifier = Modifier.padding(paddingValues)
    ){
        composable<UserInfo> {
            val state = UserInfoUiState(
                name = onboardingViewModel.name,
                email = onboardingViewModel.email,
                initialAmount = onboardingViewModel.initialAmount,
                currentUserInfoStep = onboardingViewModel.userInfoStep
            )
            UserInfoScreen(
                state = state,
                onNameChange = { onboardingViewModel.updateName(it) },
                onEmailChange = { onboardingViewModel.updateEmail(it) },
                onNext = { onboardingViewModel.goToEmailStep() },
                onNextStep = {
                    if (onboardingViewModel.completeUserInfoStep()) {
                        navController.navigate(InitialBudget)
                    }
                }
            )
        }

        composable<InitialBudget> {
            val state = UserInfoUiState(
                name = onboardingViewModel.name,
                email = onboardingViewModel.email,
                initialAmount = onboardingViewModel.initialAmount,
                currentUserInfoStep = onboardingViewModel.userInfoStep
            )
            InitialBudgetScreen(
                state = state,
                onAmountChange = { onboardingViewModel.updateInitialAmount(it) },
                onNextStep = {
                    if (onboardingViewModel.completeAddBudgetStep()) {
                        navController.navigate(InitialBudgetAllocation)
                    }
                },
                onPrevStep = {
                    navController.navigate(UserInfo)
                }
            )
        }

        composable<InitialBudgetAllocation> {
            val state = BudgetAllocationUiState(
                totalIncome = onboardingViewModel.initialAmount,
                allocations = onboardingViewModel.allocations,
                availableCategoriesForPicker = onboardingViewModel.availableCategoriesForPicker,
                isCategoryPickerVisible = onboardingViewModel.isCategoryPickerVisible,
                selectedCategoryIds = onboardingViewModel.selectedCategoryIds
            )

            // 👇 add this block, right here
            LaunchedEffect(onboardingViewModel.completionState) {
                Log.d("Onboarding", "completionState = ${onboardingViewModel.completionState}")
            }

            InitialBudgetAllocationScreen(
                state = state,
                onAmountChange = { categoryId, amountText ->
                    onboardingViewModel.updateAllocationAmount(
                        categoryId, amountText.toDoubleOrNull() ?: 0.0, amountText
                    )
                },
                onAddCategoryClick = { onboardingViewModel.openCategoryPicker() },
                onCategoryToggle = { onboardingViewModel.toggleCategorySelection(it) },
                onConfirmSelection = { onboardingViewModel.confirmCategorySelection() },
                onDismissPicker = { onboardingViewModel.dismissCategoryPicker() },
                onPrevStep = {
                    navController.navigate(InitialBudget)
                },
                onFinishClick = { onboardingViewModel.completeOnboardingStep() }
            )
        }
    }

}