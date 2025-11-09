package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.NotificationPreferencesRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.NotificationSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val repo: NotificationPreferencesRepositoryImpl = NotificationPreferencesRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationSettings())
    val uiState: StateFlow<NotificationSettings> = _uiState

    init {
        viewModelScope.launch {
            repo.observeSettings().collect { settings ->
                _uiState.value = settings
            }
        }
    }

    fun toggleAll(enabled: Boolean) {
        updateSettings { current ->
            current.copy(
                offersEnabled = enabled,
                newReleasesEnabled = enabled,
                orderUpdatesEnabled = enabled
            )
        }
    }

    fun toggleOffers(enabled: Boolean) = updateSettings { it.copy(offersEnabled = enabled) }

    fun toggleNewReleases(enabled: Boolean) = updateSettings { it.copy(newReleasesEnabled = enabled) }

    fun toggleOrderUpdates(enabled: Boolean) = updateSettings { it.copy(orderUpdatesEnabled = enabled) }

    private fun updateSettings(transform: (NotificationSettings) -> NotificationSettings) {
        viewModelScope.launch {
            repo.updateSettings { current ->
                val updated = transform(current)
                val allEnabled = updated.offersEnabled && updated.newReleasesEnabled && updated.orderUpdatesEnabled
                updated.copy(allEnabled = allEnabled)
            }
        }
    }
}
