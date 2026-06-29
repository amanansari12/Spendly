package com.amanansari.spendly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amanansari.spendly.data.local.db.DatabaseProvider
import com.amanansari.spendly.data.local.db.SpendlyDatabase
import com.amanansari.spendly.data.repository.UserRepository
import com.amanansari.spendly.register.viewmodel.UserViewModel
import com.amanansari.spendly.register.viewmodel.UserViewModelFactory
import com.amanansari.spendly.ui.theme.SpendlyTheme

class MainActivity : ComponentActivity() {

    //TODO: Migrate to the Hilt, which does not require us to write viewModel factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        val database = DatabaseProvider.getDatabase(applicationContext)

        val repository = UserRepository(database.userDao())

        val factory = UserViewModelFactory(repository)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {

            val userViewModel : UserViewModel = viewModel(factory = factory)

            SpendlyTheme {
                    MainScreen(userViewModel =  userViewModel)
            }
        }
    }
}

