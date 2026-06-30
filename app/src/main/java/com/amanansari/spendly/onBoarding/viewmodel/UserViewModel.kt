package com.amanansari.spendly.onBoarding.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.data.local.preferences.DataStoreManager
import com.amanansari.spendly.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class OnboardingStep{
    NAME,
    EMAIL
}
class UserViewModel(
    private val repository: UserRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    //? Onboarding Step
    var _name by mutableStateOf("")
        private set

    var _email by mutableStateOf("")
        private set

    var currentStep by mutableStateOf(OnboardingStep.NAME)
        private set

    fun updateName(name : String){
        _name = name
    }


    fun updateEmail(email : String){
        _email = email
    }

    fun goToNextStep(){
        if(_name.isNotBlank()) currentStep = OnboardingStep.EMAIL
    }

    fun completeOnboarding(){
        viewModelScope.launch {
            dataStoreManager.saveOnboardingState(true)
        }
    }

    val isOnboardingCompleted = dataStoreManager.onboardingState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )


    val user = repository.getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun insertUser(user : UserEntity) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }
}