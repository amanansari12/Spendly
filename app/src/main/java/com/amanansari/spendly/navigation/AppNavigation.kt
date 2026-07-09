package com.amanansari.spendly.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amanansari.spendly.MainScreen
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.navigation.graph.MainNavGraph
import com.amanansari.spendly.navigation.graph.OnboardingNavGraph

import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    onboardingViewModel: OnboardingViewModel,
) {

    val isOnboardingCompletedState by onboardingViewModel.isOnboardingCompleted.collectAsState()

    // Abhi loading ho raha hai, NavHost build hi mat kar
    if (isOnboardingCompletedState == null) return

    val isOnboardingCompleted = isOnboardingCompletedState

    if (isOnboardingCompleted == true) {

        MainScreen(onboardingViewModel)

    } else {

        val onBoardingNavController = rememberNavController()

        OnboardingNavGraph(
            navController = onBoardingNavController,
            onboardingViewModel = onboardingViewModel,
        )

    }
}