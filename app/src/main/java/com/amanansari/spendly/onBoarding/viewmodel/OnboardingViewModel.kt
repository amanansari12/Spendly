package com.amanansari.spendly.onBoarding.viewmodel

import android.os.Build
import android.util.Log.e
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanansari.spendly.data.local.entity.MonthlyBudgetEntity
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.data.local.preferences.DataStoreManager


import com.amanansari.spendly.data.repository.OnboardingRepository
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

sealed class OnboardingCompletionState {
    object Idle : OnboardingCompletionState()
    object Loading : OnboardingCompletionState()
    object Success : OnboardingCompletionState()
    data class Error(val message: String) : OnboardingCompletionState()
}


class OnboardingViewModel(
    private val userRepository: UserRepository,
    private val onboardingRepository: OnboardingRepository,
    private val dataStoreManager: DataStoreManager
    ) : ViewModel() {

    var completionState by mutableStateOf<OnboardingCompletionState>(OnboardingCompletionState.Idle)
        private set

    //? Onboarding Step
    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var userInfoStep by mutableStateOf(UserInfoStep.NAME)
        private set

    var currentStep by mutableStateOf(OnboardingStep.USER_INFO)
        private set

    var initialAmount by mutableDoubleStateOf(0.0)
        private set

    fun updateName(name : String){
        this.name = name
    }

    fun updateEmail(email : String){
        this.email = email
    }

    fun updateInitialAmount(amount : Double){
        this.initialAmount = amount
    }

    fun goToEmailStep() {
        if (name.isNotBlank()) userInfoStep = UserInfoStep.EMAIL
    }

    fun completeUserInfoStep(){
        if(email.isNotBlank()) currentStep = OnboardingStep.INITIAL_BALANCE
    }

//    fun CompletedInitialAmount(){
//        if(initialAmount != 0.0) currentStep =
//    }


    val isOnboardingCompleted = dataStoreManager.onboardingState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    /*TODO: The Main Screen is Fetching Name from this ViewModel which will be Updated.
       The Name will be fetched from the MainScreenViewModel
     */

    val user = userRepository.getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun completeOnboardingStep(){

        viewModelScope.launch {
            if (name.isBlank() || email.isBlank()) {
                completionState = OnboardingCompletionState.Error("Missing user info")
                return@launch
            }

            try{
                completionState = OnboardingCompletionState.Loading
                val user = UserEntity(name = name, email = email)
                onboardingRepository.completeOnboarding(user, initialAmount)
                completionState = OnboardingCompletionState.Success
            }
            catch (e: Exception){
                completionState = OnboardingCompletionState.Error(e.message ?: "Failed to complete onboarding")

            }
        }

    }

}