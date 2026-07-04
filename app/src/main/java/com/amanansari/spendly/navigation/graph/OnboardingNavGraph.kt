package com.amanansari.spendly.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amanansari.spendly.navigation.route.InitialBalance
import com.amanansari.spendly.navigation.route.UserInfo
import com.amanansari.spendly.onBoarding.route.OnboardingRoute
import com.amanansari.spendly.onBoarding.screen.UserInfoScreen
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel

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
            OnboardingRoute(onboardingViewModel, onNext = {
                navController.navigate(InitialBalance)
            })
        }

        composable<InitialBalance> {

        }
    }

}