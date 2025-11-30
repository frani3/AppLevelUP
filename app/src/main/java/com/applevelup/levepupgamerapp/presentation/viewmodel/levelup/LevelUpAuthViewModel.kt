package com.applevelup.levepupgamerapp.presentation.viewmodel.levelup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.model.levelup.LoginCredentials
import com.applevelup.levepupgamerapp.domain.model.levelup.RegistrationData
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchUserProfileUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.LoginUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.LogoutUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.RegisterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LevelUpAuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val fetchProfileUseCase: FetchUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val profileState = fetchProfileUseCase(forceRefresh = false)
        .stateIn(viewModelScope, SharingStarted.Lazily, LevelUpResource.Loading)

    private val _authEvent = MutableSharedFlow<LevelUpResult<LevelUpUserProfile>>()
    val authEvent: SharedFlow<LevelUpResult<LevelUpUserProfile>> = _authEvent

    init {
        refreshProfile()
    }

    fun login(credentials: LoginCredentials) {
        viewModelScope.launch {
            val result = loginUseCase(credentials)
            _authEvent.emit(result)
            if (result is LevelUpResult.Success) {
                refreshProfile(force = true)
            }
        }
    }

    fun register(registrationData: RegistrationData) {
        viewModelScope.launch {
            val result = registerUseCase(registrationData)
            _authEvent.emit(result)
            if (result is LevelUpResult.Success) {
                refreshProfile(force = true)
            }
        }
    }

    private fun refreshProfile(force: Boolean = false) {
        viewModelScope.launch {
            fetchProfileUseCase(force).first()
        }
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }
}
