package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class AddAddressUiState(
    val fullName: String = "",
    val line1: String = "",
    val city: String = "",
    val region: String = "",
    val setAsDefault: Boolean = true,
    val isValid: Boolean = false,
    val showValidationErrors: Boolean = false,
    val isSaving: Boolean = false
)

class AddAddressViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddAddressUiState())
    val uiState: StateFlow<AddAddressUiState> = _uiState

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value) }
        validate()
    }

    fun onLine1Change(value: String) {
        _uiState.update { it.copy(line1 = value) }
        validate()
    }

    fun onCityChange(value: String) {
        _uiState.update { it.copy(city = value) }
        validate()
    }

    fun onRegionChange(value: String) {
        _uiState.update { it.copy(region = value) }
        validate()
    }

    fun onDefaultChange(value: Boolean) {
        _uiState.update { it.copy(setAsDefault = value) }
    }

    fun setSaving(value: Boolean) {
        _uiState.update { it.copy(isSaving = value) }
    }

    fun resetForm() {
        _uiState.value = AddAddressUiState()
    }

    fun buildAddressInput(): AddressInput? {
        val current = _uiState.value
        if (!current.isValid) {
            _uiState.update { it.copy(showValidationErrors = true) }
            return null
        }
        return AddressInput(
            fullName = current.fullName.trim(),
            line1 = current.line1.trim(),
            city = current.city.trim(),
            region = current.region.trim(),
            isPrimary = current.setAsDefault
        )
    }

    private fun validate() {
        val current = _uiState.value
        val valid = current.fullName.isNotBlank() &&
            current.line1.isNotBlank() &&
            current.city.isNotBlank() &&
            current.region.isNotBlank()
        _uiState.update { it.copy(isValid = valid) }
    }
}
