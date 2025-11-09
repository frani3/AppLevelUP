package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.applevelup.levepupgamerapp.data.repository.AddressRepositoryImpl
import com.applevelup.levepupgamerapp.domain.usecase.AddAddressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class AddAddressUiState(
    val alias: String = "",
    val street: String = "",
    val city: String = "",
    val details: String = "",
    val setAsDefault: Boolean = true,
    val isValid: Boolean = false,
    val showValidationErrors: Boolean = false
)

class AddAddressViewModel(
    private val repository: AddressRepositoryImpl = AddressRepositoryImpl()
) : ViewModel() {

    private val addAddressUseCase = AddAddressUseCase(repository)

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

    fun onCityChange(value: String) {
        _uiState.update { it.copy(city = value) }
        validate()
    }

    fun onDetailsChange(value: String) {
        _uiState.update { it.copy(details = value) }
    }

    fun onDefaultChange(value: Boolean) {
        _uiState.update { it.copy(setAsDefault = value) }
    }

    private fun validate() {
        val current = _uiState.value
        val valid = current.alias.isNotBlank() && current.street.isNotBlank() && current.city.isNotBlank()
        _uiState.update { it.copy(isValid = valid) }
    }

    fun saveAddress(): Boolean {
        val current = _uiState.value
        if (!current.isValid) {
            _uiState.update { it.copy(showValidationErrors = true) }
            return false
        }
        addAddressUseCase(
            alias = current.alias,
            street = current.street,
            city = current.city,
            details = current.details,
            setAsDefault = current.setAsDefault
        )
        _uiState.value = AddAddressUiState()
        return true
    }
}
