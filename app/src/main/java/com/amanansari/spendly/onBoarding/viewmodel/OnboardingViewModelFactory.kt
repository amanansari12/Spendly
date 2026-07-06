package com.amanansari.spendly.onBoarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amanansari.spendly.data.local.preferences.DataStoreManager
import com.amanansari.spendly.data.repository.OnboardingRepository
import com.amanansari.spendly.data.repository.UserRepository

class OnboardingViewModelFactory(
    private val userRepository: UserRepository,
    private val onboardingRepository: OnboardingRepository,
    private val dataStoreManager: DataStoreManager

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OnboardingViewModel(userRepository, onboardingRepository, dataStoreManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}