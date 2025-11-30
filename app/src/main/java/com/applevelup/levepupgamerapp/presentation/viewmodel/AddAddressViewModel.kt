package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class AddAddressUiState(
    val alias: String = "",
    val street: String = "",
    val number: String = "",
    val comuna: String = "",
    val region: String = "",
    val details: String = "",
    val setAsDefault: Boolean = true,
    val isValid: Boolean = false,
    val showValidationErrors: Boolean = false,
    val isSaving: Boolean = false
)

class AddAddressViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddAddressUiState())
    val uiState: StateFlow<AddAddressUiState> = _uiState

    fun onAliasChange(value: String) {
        _uiState.update { it.copy(alias = value) }
        validate()
    }

    fun onStreetChange(value: String) {
        _uiState.update { it.copy(street = value) }
        validate()
    }

    fun onNumberChange(value: String) {
        _uiState.update { it.copy(number = value) }
    }

    fun onComunaChange(value: String) {
        _uiState.update { it.copy(comuna = value) }
        validate()
    }

    fun onRegionChange(value: String) {
        _uiState.update { it.copy(region = value) }
        validate()
    }

    fun onDetailsChange(value: String) {
        _uiState.update { it.copy(details = value) }
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
            alias = current.alias.trim(),
            direccion = current.street.trim(),
            numero = current.number.trim().ifBlank { null },
            comuna = current.comuna.trim(),
            region = current.region.trim(),
            isPrimary = current.setAsDefault,
            complement = current.details.trim().ifBlank { null }
        )
    }

    private fun validate() {
        val current = _uiState.value
        val valid = current.alias.isNotBlank() &&
            current.street.isNotBlank() &&
            current.comuna.isNotBlank() &&
            current.region.isNotBlank()
        _uiState.update { it.copy(isValid = valid) }
    }
}
