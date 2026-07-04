package com.amanansari.spendly

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amanansari.spendly.data.local.db.DatabaseProvider
import com.amanansari.spendly.data.local.preferences.DataStoreManager
import com.amanansari.spendly.data.repository.MonthlyBudgetRepository
import com.amanansari.spendly.data.repository.UserRepository
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModel
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingViewModelFactory
import com.amanansari.spendly.ui.theme.SpendlyTheme

class MainActivity : ComponentActivity() {

    //TODO: Migrate to the Hilt, which does not require us to write viewModel factory
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        val database = DatabaseProvider.getDatabase(applicationContext)

        val userRepository = UserRepository(database.userDao())
        val monthlyBudgetRepository = MonthlyBudgetRepository(database.monthlyBudgetDao())

        val dataStoreManager = DataStoreManager(applicationContext)

        val factory = OnboardingViewModelFactory(userRepository, monthlyBudgetRepository, dataStoreManager)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )


        setContent {

            val onboardingViewModel : OnboardingViewModel = viewModel(factory = factory)

            splashScreen.setKeepOnScreenCondition {
                onboardingViewModel.isOnboardingCompleted.value == null
            }

            SpendlyTheme {
                    MainScreen(onboardingViewModel =  onboardingViewModel)
            }
        }
    }
}

