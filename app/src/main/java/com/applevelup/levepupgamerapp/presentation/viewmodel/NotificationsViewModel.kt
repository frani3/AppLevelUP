package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.applevelup.levepupgamerapp.data.repository.NotificationPreferencesRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.NotificationSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NotificationsViewModel(
    private val repo: NotificationPreferencesRepositoryImpl = NotificationPreferencesRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(repo.getSettings())
    val uiState: StateFlow<NotificationSettings> = _uiState

    fun toggleAll(enabled: Boolean) {
        val updated = _uiState.value.copy(allEnabled = enabled)
        _uiState.update { updated.copy(
            offersEnabled = enabled,
            newReleasesEnabled = enabled,
            orderUpdatesEnabled = enabled
        ) }
        repo.saveSettings(_uiState.value)
    }

    fun toggleOffers(enabled: Boolean) {
        _uiState.update { it.copy(offersEnabled = enabled) }
        repo.saveSettings(_uiState.value)
    }

    fun toggleNewReleases(enabled: Boolean) {
        _uiState.update { it.copy(newReleasesEnabled = enabled) }
        repo.saveSettings(_uiState.value)
    }

    fun toggleOrderUpdates(enabled: Boolean) {
        _uiState.update { it.copy(orderUpdatesEnabled = enabled) }
        repo.saveSettings(_uiState.value)
    }
}
