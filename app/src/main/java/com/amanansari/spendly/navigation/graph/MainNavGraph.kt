package com.amanansari.spendly.navigation.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.amanansari.spendly.home.screen.HomeOverviewScreen
import com.amanansari.spendly.home.screen.HomeScreen
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.navigation.route.AddTransaction
import com.amanansari.spendly.navigation.route.Home
import com.amanansari.spendly.navigation.route.Analytics
import com.amanansari.spendly.navigation.route.Budget
import com.amanansari.spendly.navigation.route.Profile
import com.amanansari.spendly.navigation.route.TransactionHistory
import com.amanansari.spendly.transaction.screen.TransactionScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    homePagerState: PagerState,

    ) {

    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = Modifier.padding(paddingValues)
    ) {

        composable<Home>{
             HomeOverviewScreen(onQuickSelect = { category ->
                        navController.navigate(AddTransaction(category.id))
                    },
                 pagerState = homePagerState,
             )
         }

        composable<AddTransaction> { backstackEntry ->

            val args = backstackEntry.toRoute<AddTransaction>()
            TransactionScreen(
                onClose = {
                    navController.popBackStack()
                },

                quickSelectedCategoryId = args.categoryId

            )
            
        }

        composable<TransactionHistory> {
            Text("Transaction History screen — coming soon")
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