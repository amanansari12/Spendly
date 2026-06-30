package com.amanansari.spendly.onBoarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amanansari.spendly.data.local.preferences.DataStoreManager
import com.amanansari.spendly.data.repository.UserRepository

class UserViewModelFactory(
    private val repository: UserRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository, dataStoreManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}