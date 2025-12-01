package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpRegion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddAddressUiState(
    val fullName: String = "",
    val line1: String = "",
    val selectedRegion: LevelUpRegion? = null,
    val selectedComuna: String = "",
    val regions: List<LevelUpRegion> = emptyList(),
    val comunas: List<String> = emptyList(),
    val isRegionDropdownExpanded: Boolean = false,
    val isComunaDropdownExpanded: Boolean = false,
    val setAsDefault: Boolean = false,
    val isValid: Boolean = false,
    val showValidationErrors: Boolean = false,
    val isSaving: Boolean = false,
    val isLoadingRegions: Boolean = true
)

class AddAddressViewModel : ViewModel() {

    private val regionRepository = LevelUpDependencyContainer.regionRepository

    private val _uiState = MutableStateFlow(AddAddressUiState())
    val uiState: StateFlow<AddAddressUiState> = _uiState

    init {
        loadRegions()
    }

    private fun loadRegions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingRegions = true) }
            regionRepository.refreshRegions()
            regionRepository.observeRegions().collect { regions ->
                _uiState.update { 
                    it.copy(
                        regions = regions,
                        isLoadingRegions = false
                    )
                }
            }
        }
    }

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value) }
        validate()
    }

    fun onLine1Change(value: String) {
        _uiState.update { it.copy(line1 = value) }
        validate()
    }

    fun onRegionSelected(region: LevelUpRegion) {
        _uiState.update { 
            it.copy(
                selectedRegion = region,
                comunas = region.comunas,
                selectedComuna = "", // Reset comuna when region changes
                isRegionDropdownExpanded = false
            )
        }
        validate()
    }

    fun onComunaSelected(comuna: String) {
        _uiState.update { 
            it.copy(
                selectedComuna = comuna,
                isComunaDropdownExpanded = false
            )
        }
        validate()
    }

    fun toggleRegionDropdown() {
        _uiState.update { it.copy(isRegionDropdownExpanded = !it.isRegionDropdownExpanded) }
    }

    fun toggleComunaDropdown() {
        val current = _uiState.value
        if (current.selectedRegion != null) {
            _uiState.update { it.copy(isComunaDropdownExpanded = !it.isComunaDropdownExpanded) }
        }
    }

    fun dismissRegionDropdown() {
        _uiState.update { it.copy(isRegionDropdownExpanded = false) }
    }

    fun dismissComunaDropdown() {
        _uiState.update { it.copy(isComunaDropdownExpanded = false) }
    }

    fun onDefaultChange(value: Boolean) {
        _uiState.update { it.copy(setAsDefault = value) }
    }

    fun setSaving(value: Boolean) {
        _uiState.update { it.copy(isSaving = value) }
    }

    fun resetForm() {
        _uiState.value = AddAddressUiState(
            regions = _uiState.value.regions,
            isLoadingRegions = false
        )
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
            city = current.selectedComuna.trim(),
            region = current.selectedRegion?.name?.trim() ?: "",
            isPrimary = current.setAsDefault
        )
    }

    private fun validate() {
        val current = _uiState.value
        val valid = current.fullName.isNotBlank() &&
            current.line1.isNotBlank() &&
            current.selectedRegion != null &&
            current.selectedComuna.isNotBlank()
        _uiState.update { it.copy(isValid = valid) }
    }
}
