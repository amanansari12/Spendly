package com.amanansari.spendly.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "app_preferences"
)

class DataStoreManager @Inject constructor (
    @ApplicationContext private val context: Context
){
    companion object{
        val ONBOARDING_COMPLETED =
            booleanPreferencesKey("onboarding_completed")
    }

    suspend fun saveOnboardingState(isCompleted : Boolean){
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = isCompleted
        }
    }

    val onboardingState: Flow<Boolean?> =
        context.dataStore.data.map { preferences ->
            preferences[ONBOARDING_COMPLETED] ?: false
        }

}