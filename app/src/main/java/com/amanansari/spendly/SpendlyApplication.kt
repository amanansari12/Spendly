package com.amanansari.spendly

import com.amanansari.spendly.data.repository.CategoryRepository
import android.app.Application
import com.amanansari.spendly.data.local.db.DatabaseProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

import com.amanansari.spendly.data.local.db.SpendlyDatabase
import com.amanansari.spendly.data.local.preferences.DataStoreManager
import com.amanansari.spendly.data.repository.OnboardingRepository
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class SpendlyApplication : Application() {

    // 1. A place to catch any crash that happens inside our background work,
    //    so a seeding failure never brings down the whole app silently.
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    // 2. Application has NO built-in coroutine scope (unlike ViewModel's viewModelScope).
    //    So we make our own, tied to the whole app's lifetime.
    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + exceptionHandler
    )

    // 3. Expose the database so other parts of the app (your ViewModelFactory) can reuse
    //    this SAME instance instead of creating a second one.
    lateinit var database: SpendlyDatabase
        private set

    @Inject
    lateinit var categoryRepository: CategoryRepository

    override fun onCreate() {
        super.onCreate()

        database = DatabaseProvider.getDatabase(this)

        applicationScope.launch {
            categoryRepository.ensureSeeded()   // fine if this finishes late — see step 3
        }
    }
}