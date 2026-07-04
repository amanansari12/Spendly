package com.amanansari.spendly

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amanansari.spendly.home.screen.getInitials
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel
import com.amanansari.spendly.ui.theme.DarkOverlay
import com.amanansari.spendly.ui.theme.LightTextPrimary
import com.amanansari.spendly.ui.theme.LightTextSecondary
import com.amanansari.spendly.ui.theme.LightTintedBg
import com.amanansari.spendly.ui.theme.Primary
import androidx.compose.runtime.collectAsState

@Composable
fun TopBar(navController: NavController, onboardingViewModel: OnboardingViewModel) {

    // 1. Observe the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    when(currentRoute){
        "home" -> HomeTopBar(onboardingViewModel)
//        "budget" -> BudgetTopBar()
        "analytics" -> AnalyticsTopBar()
//        "profile" -> ProfileTopBar()

    }


}


@Composable
fun HomeTopBar(onboardingViewModel: OnboardingViewModel){

    val user by onboardingViewModel.user.collectAsState()

    val firstname = user?.name?.trim()?.split(" ")?.firstOrNull() ?: "User"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Good Morning",
                fontSize = 14.sp,
                color = LightTextSecondary
            )
            Text(
                text = "Hi, $firstname 👋",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LightTextPrimary
            )
        }

        val profileImg = true

        if (profileImg)
            Box(
                modifier = Modifier
                    .size(40.dp) // outer circle size (keep this same)
                    .clip(CircleShape)
                    .border(1.5.dp, Primary, CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center,

            ) {
                Image(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(25.dp), // 👈 reduce icon size here
                    contentScale = ContentScale.Crop
                )
            }
        else
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LightTintedBg)
                    .border(1.5.dp, Primary, CircleShape),

                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials("Aman Ansari"),
                    color = DarkOverlay,
                    fontWeight = FontWeight.Bold
                )
            }


    }
}

@Composable
fun AnalyticsTopBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Analytics",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LightTextPrimary
        )

        Icon(
            imageVector = Icons.Rounded.Notifications,
            contentDescription = null,
            tint = Primary
        )
    }
}