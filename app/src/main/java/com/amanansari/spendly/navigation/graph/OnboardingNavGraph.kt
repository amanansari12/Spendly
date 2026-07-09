package com.amanansari.spendly.navigation.graph

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amanansari.spendly.navigation.route.InitialBudget
import com.amanansari.spendly.navigation.route.InitialBudgetAllocation
import com.amanansari.spendly.navigation.route.UserInfo
import com.amanansari.spendly.navigation.route.IncomeSource
import com.amanansari.spendly.onBoarding.screen.IncomeSourceScreen
import com.amanansari.spendly.onBoarding.screen.InitialBudgetAllocationScreen
import com.amanansari.spendly.onBoarding.screen.InitialBudgetScreen
import com.amanansari.spendly.onBoarding.screen.UserInfoScreen
import com.amanansari.spendly.onBoarding.state.BudgetAllocationUiState
import com.amanansari.spendly.onBoarding.state.UserInfoUiState
import com.amanansari.spendly.onBoarding.state.IncomeSourceUistate
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnboardingNavGraph(
    navController: NavHostController,
    onboardingViewModel: OnboardingViewModel,

){

    NavHost(
        navController = navController,
        startDestination = UserInfo,
        modifier = Modifier.statusBarsPadding()
    ){
        composable<UserInfo> {
            val state = UserInfoUiState(
                name = onboardingViewModel.name,
                email = onboardingViewModel.email,
                initialAmount = onboardingViewModel.initialAmount,
                currentUserInfoStep = onboardingViewModel.userInfoStep,
                currency = onboardingViewModel.currency
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
                amountFieldValue = onboardingViewModel.amountFieldValue,
                currentUserInfoStep = onboardingViewModel.userInfoStep,
                currency = onboardingViewModel.currency
            )

            BackHandler() {
                onboardingViewModel.resetInitialBudget()
                navController.popBackStack()
            }

            InitialBudgetScreen(
                state = state,
                onAmountChange = { amount, newValue ->
                    onboardingViewModel.updateInitialAmount(amount, newValue)
                                 },
                onNextStep = {
                    if (onboardingViewModel.completeAddBudgetStep()) {
                        navController.navigate(IncomeSource)
                    }
                },
                onPrevStep = {
                    onboardingViewModel.resetInitialBudget()
                    navController.popBackStack()

                }
            )
        }

        composable<IncomeSource> {

            val state = IncomeSourceUistate(
                availableIncomeSource = onboardingViewModel.availableIncomeSource,
                selectedIncomeSourceId =onboardingViewModel.selectedIncomeSourceId
            )

            BackHandler() {
                onboardingViewModel.resetIncomeSelection()
                navController.popBackStack()
            }

            IncomeSourceScreen(
                state = state,
                onContinue = {
                    if (onboardingViewModel.completeIncomeSourceStep()) {
                    navController.navigate(InitialBudgetAllocation)
                    }},
                onSkip = {
                    onboardingViewModel.resetIncomeSelection()
                    navController.navigate(InitialBudgetAllocation)
                },
                onIncomeToggle = {onboardingViewModel.toggleIncome(it)},
                onPrevStep = {
                    onboardingViewModel.resetIncomeSelection()
                    navController.popBackStack()

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


            LaunchedEffect(onboardingViewModel.completionState) {
                Log.d("Onboarding", "completionState = ${onboardingViewModel.completionState}")
            }

            BackHandler(){
                onboardingViewModel.removeAllocations()
                navController.popBackStack()
            }

            InitialBudgetAllocationScreen(
                state = state,
                onAmountChange = { categoryId, amountText ->
                    onboardingViewModel.updateAllocationAmount(
                        categoryId, amountText.toDoubleOrNull() ?: 0.0, amountText
                    )
                },
                onRemoveCategoryClick = {
                    onboardingViewModel.removeCategoryFromAllocation(it)
                },
                onAddCategoryClick = { onboardingViewModel.openCategoryPicker() },
                onCategoryToggle = { onboardingViewModel.toggleCategorySelection(it) },
                onConfirmSelection = { onboardingViewModel.confirmCategorySelection() },
                onDismissPicker = { onboardingViewModel.dismissCategoryPicker() },
                onPrevStep = {
                    onboardingViewModel.removeAllocations()
                    navController.popBackStack()

                },

            ) { onboardingViewModel.completeOnboardingStep() }
        }
    }

}