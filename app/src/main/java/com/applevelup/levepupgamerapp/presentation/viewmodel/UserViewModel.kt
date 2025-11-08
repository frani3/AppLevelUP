package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.UserRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserUiState(
    val profile: UserProfile? = null,
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

class UserViewModel(
    private val useCase: GetUserProfileUseCase = GetUserProfileUseCase(UserRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    init {
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            val profile = useCase.getUserProfile()
            val orders = useCase.getOrders()
            _uiState.update { UserUiState(profile, orders, isLoading = false) }
        }
    }

    fun logout() {
        useCase.logout()
    }
}
