package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.data.repository.UserRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.usecase.GetUserProfileUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class UserUiState(
    val profile: UserProfile? = null,
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

class UserViewModel(
    private val useCase: GetUserProfileUseCase = GetUserProfileUseCase(UserRepositoryImpl())
) : ViewModel() {

    private val levelUpRepository = LevelUpDependencyContainer.userRepository
    private val fetchLevelUpProfile = FetchUserProfileUseCase(levelUpRepository)
    private val authRepository = LevelUpDependencyContainer.authRepository

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    private val _logoutEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val logoutEvents = _logoutEvents.asSharedFlow()

    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val messages = _messages.asSharedFlow()

    init {
        observeLevelUpProfile()
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            val orders = useCase.getOrders()
            _uiState.update { it.copy(orders = orders, isLoading = false) }
            refreshLevelUpProfile()
        }
    }

    fun logout() {
        viewModelScope.launch {
            useCase.logout()
            authRepository.logout()
            _logoutEvents.emit(Unit)
        }
    }

    fun updateProfilePhoto(photoUri: String) {
        viewModelScope.launch {
            runCatching {
                useCase.updateProfilePhoto(photoUri)
            }.onSuccess {
                loadUserData()
                _messages.emit("Foto de perfil actualizada")
            }.onFailure {
                _messages.emit("No se pudo actualizar la foto de perfil")
            }
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

    private fun refreshLevelUpProfile(force: Boolean = true) {
        viewModelScope.launch {
            runCatching {
                fetchLevelUpProfile(force).first()
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
