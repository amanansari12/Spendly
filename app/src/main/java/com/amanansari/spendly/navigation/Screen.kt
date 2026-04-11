package com.amanansari.spendly.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val title : String, val route : String) {


    object Transactions : Screen("Transactions", "transactions")
    object EditBudget : Screen("Edit Budget", "edit_budget")
    sealed class BottomNav(val bTitle : String, val bRoute : String, val icon : ImageVector) : Screen(bTitle, bRoute) {

        object Home : BottomNav("Home", "home", Icons.Rounded.Home)
        object Analytics : BottomNav("Analytics", "analytics", Icons.Rounded.AutoGraph)
        object Budget : BottomNav("Budget", "budget", Icons.Rounded.AccountBalanceWallet)
        object Profile : BottomNav("Profile", "profile", Icons.Rounded.Person)
    }
}

val screens = listOf<Screen.BottomNav>(
    Screen.BottomNav.Home,
    Screen.BottomNav.Analytics,
    Screen.BottomNav.Budget,
    Screen.BottomNav.Profile
)