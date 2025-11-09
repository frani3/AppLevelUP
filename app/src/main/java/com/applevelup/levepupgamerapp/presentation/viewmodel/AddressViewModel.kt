package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.AddressRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.usecase.DeleteAddressUseCase
import com.applevelup.levepupgamerapp.domain.usecase.GetAddressesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddressUiState(
    val addresses: List<Address> = emptyList(),
    val isLoading: Boolean = true
)

class AddressViewModel(
    private val repo: AddressRepositoryImpl = AddressRepositoryImpl()
) : ViewModel() {

    private val getAddresses = GetAddressesUseCase(repo)
    private val deleteAddress = DeleteAddressUseCase(repo)

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState

    init {
        loadAddresses()
    }

    fun loadAddresses() {
        viewModelScope.launch {
            _uiState.update { it.copy(addresses = getAddresses(), isLoading = false) }
        }
    }

    fun removeAddress(id: Int) {
        viewModelScope.launch {
            deleteAddress(id)
            loadAddresses()
        }
    }

    fun setDefault(id: Int) {
        viewModelScope.launch {
            repo.setDefaultAddress(id)
            loadAddresses()
        }
    }
}
