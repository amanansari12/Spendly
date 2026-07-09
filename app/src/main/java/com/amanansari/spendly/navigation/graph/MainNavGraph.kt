package com.amanansari.spendly.navigation.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amanansari.spendly.home.screen.HomeScreen
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.navigation.route.AddTransaction
import com.amanansari.spendly.navigation.route.Home
import com.amanansari.spendly.navigation.route.Analytics
import com.amanansari.spendly.navigation.route.Budget
import com.amanansari.spendly.navigation.route.Profile
import com.amanansari.spendly.transaction.screen.AddTransactionScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
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

        composable<AddTransaction> {
            AddTransactionScreen(
                onClose = {
                    navController.popBackStack()
                }
            )
            
        }

        composable<Budget> {
            Text("Budget screen — coming soon")
        }

        composable<Profile> {
            Text("Profile screen — coming soon")
        }

        composable<Analytics> {
            Text("Analytics screen — coming soon")
        }



    }
}