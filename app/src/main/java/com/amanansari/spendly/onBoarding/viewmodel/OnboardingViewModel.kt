package com.amanansari.spendly.onBoarding.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.data.local.preferences.DataStoreManager
import com.amanansari.spendly.data.repository.MonthlyBudgetRepository
import com.amanansari.spendly.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class OnboardingStep{

    USER_INFO,
    INITIAL_BALANCE,
}

enum class UserInfoStep{
    NAME,
    EMAIL
}
class OnboardingViewModel(
    private val userRepository: UserRepository,
    private val monthlyBudgetRepository: MonthlyBudgetRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    //? Onboarding Step
    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var userInfoStep by mutableStateOf(UserInfoStep.NAME)
        private set

    var currentStep by mutableStateOf(OnboardingStep.USER_INFO)
        private set

    fun updateName(name : String){
        this.name = name
    }


    fun updateEmail(email : String){
        this.email = email
    }

//    fun goToNextStep(){
//        if(name.isNotBlank()) currentStep = OnboardingStep.EMAIL
//    }

    fun goToEmailStep() {
        if (name.isNotBlank()) userInfoStep = UserInfoStep.EMAIL
    }

    fun completeUserInfoStep(){
        if(email.isNotBlank()) currentStep = OnboardingStep.INITIAL_BALANCE
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


    val user = userRepository.getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun insertUser(user : UserEntity) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }
}