package com.amanansari.spendly.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.amanansari.spendly.navigation.route.Analytics
import com.amanansari.spendly.navigation.route.Budget
import com.amanansari.spendly.navigation.route.Home
import com.amanansari.spendly.navigation.route.Profile

data class BottomBarItem<T : Any>(
    val title : String,
    val icon : ImageVector,
    val route : T
)

val bottomBarItems = listOf(
    BottomBarItem(
        title = "Home",
        icon = Icons.Rounded.Home,
        route = Home
    ),
    BottomBarItem(
        title = "Analytics",
        icon = Icons.Rounded.AutoGraph,
        route = Analytics
    ),
    BottomBarItem(
        title = "Budget",
        icon = Icons.Rounded.AccountBalanceWallet,
        route = Budget
    ),
    BottomBarItem(
        title = "Profile",
        icon = Icons.Rounded.Person,
        route = Profile
    )
)