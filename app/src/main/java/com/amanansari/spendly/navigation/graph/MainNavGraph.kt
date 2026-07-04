package com.amanansari.spendly.navigation.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amanansari.spendly.home.screen.HomeScreen
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.navigation.route.Home
import com.amanansari.spendly.navigation.route.UserInfo
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onboardingViewModel: OnboardingViewModel,
    onCategorySelected: (ExpIncCategory) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = Modifier.padding(paddingValues)
    ) {

         composable<Home>{
             HomeScreen(onClickSheet = { category ->
                        onCategorySelected(category)
                    }
             )
         }

        // composable<Analytics>{}



    }
}