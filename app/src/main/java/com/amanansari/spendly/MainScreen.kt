package com.amanansari.spendly


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.navigation.AppNavigation
import com.amanansari.spendly.navigation.BottomBarItem
import com.amanansari.spendly.navigation.bottomBarItems
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel
import com.amanansari.spendly.transaction.screen.AddTxScreen
import com.amanansari.spendly.ui.theme.LightBg
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.SpendlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(onboardingViewModel: OnboardingViewModel){

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedCategory  by remember { mutableStateOf< ExpIncCategory?>(null) }
    var selectedDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = LightBg,
        topBar = {TopBar(navController, onboardingViewModel)},
        bottomBar = {
            if (currentRoute in listOf("home", "budget", "profile", "analytics")) {
                BottomNavBar(navController)
            }
        },

        floatingActionButton = {
            if(currentRoute == "home"){
                FloatingActionBtn(
                    onClick = {
                        showBottomSheet = true
                        if(selectedDate == null){
                            selectedDate = System.currentTimeMillis()
                        }
                    }
                )
            }
        }

    ) { paddingValues ->

        AppNavigation(
            navController = navController,
            paddingValues = paddingValues,
            onboardingViewModel = onboardingViewModel,
            onCategorySelected = {
                selectedCategory = it
                showBottomSheet = true
            }
        )



        AddTxScreen(showBottomSheet,
            onClick = { showBottomSheet = true},
            selectedCategory = selectedCategory,
            onCategoryChange = { category -> selectedCategory = category },
            selectedDate = selectedDate,
            onDateChange = {selectedDate = it}
        )

    }

}


//? Bottom APP Bar
@Composable
fun BottomNavBar(navController: NavHostController){

    NavigationBar(
            modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .shadow(10.dp, RoundedCornerShape(24.dp)),
            containerColor = LightSurface,
            contentColor = Primary,
            tonalElevation = 8.dp
    ) {
            //? It gives you the current screen entry from the navigation back stack
            //? currentBackStackEntryAsState() always points to the top of this stack
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            //? Extracts the route (screen name) of the current destination
            val currentRoute = navBackStackEntry?.destination?.route

        bottomBarItems.forEach { screen ->

                NavigationBarItem(
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (currentRoute == screen.route)
                                        Primary.copy(alpha = 0.15f)
                                    else
                                        Color.Transparent
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)

                        ){
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = screen.title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentRoute == screen.route) Primary else LightNavInactive
                            )
                        }
                    },
                selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            //* Pop up to the start destination of the graph to
                            //* avoid building up a large stack of destinations
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            //* Avoid multiple copies of the same destination when
                            //* reselecting the same item
                            launchSingleTop = true
                            //* Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Primary,
                        unselectedIconColor = LightNavInactive,
                        selectedTextColor = Primary,
                        unselectedTextColor = LightNavInactive,
                        indicatorColor = Color.Transparent
                    )

                )

            }
    }

}

@Composable
fun FloatingActionBtn(onClick : () -> Unit){
    FloatingActionButton(
        onClick = onClick,
        containerColor = Primary,
        modifier = Modifier
            .padding(16.dp)
            .size(56.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add Transaction",
            tint = Color.White

        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SpendlyTheme {
//        MainScreen()
    }
}