package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpOrder
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class UserUiState(
    val profile: UserProfile? = null,
    val orders: List<LevelUpOrder> = emptyList(),
    val isLoading: Boolean = true
)

class UserViewModel : ViewModel() {

    private val levelUpRepository = LevelUpDependencyContainer.userRepository
    private val authRepository = LevelUpDependencyContainer.authRepository
    private val orderRepository = LevelUpDependencyContainer.orderRepository

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    private val _logoutEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val logoutEvents = _logoutEvents.asSharedFlow()

    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val messages = _messages.asSharedFlow()

    init {
        observeLevelUpProfile()
        observeOrders()
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            levelUpRepository.refreshProfile()
            orderRepository.refreshOrders()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutEvents.emit(Unit)
        }
    }

    fun updateProfilePhoto(photoUri: String) {
        viewModelScope.launch {
            // TODO: Implementar endpoint de actualización de foto en API
            _messages.emit("Función no disponible aún")
        }
    }

    private fun observeLevelUpProfile() {
        viewModelScope.launch {
            levelUpRepository.observeProfile().collectLatest { profile ->
                val mapped = profile?.toDomain()
                _uiState.update { it.copy(profile = mapped) }
            }
        }
    }

    private fun observeOrders() {
        viewModelScope.launch {
            orderRepository.observeOrders().collectLatest { orders ->
                _uiState.update { it.copy(orders = orders) }
            }
        }
    }
}

private fun LevelUpUserProfile.toDomain(): UserProfile {
    val stats = this.stats
    val experience = stats?.experience
    return UserProfile(
        name = this.name,
        email = this.email,
        avatarRes = R.drawable.avatar_placeholder,
        photoUri = null,
        orderCount = experience?.compras ?: 0,
        wishlistCount = experience?.torneos ?: 0,
        couponCount = experience?.referidos ?: 0,
        run = this.run,
        profileRole = "Cliente",
        birthDate = null,
        region = this.region,
        comuna = this.commune,
        address = this.address,
        hasLifetimeDiscount = !stats?.referralCode.isNullOrBlank(),
        isSystem = false
    )
}
