package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.applevelup.levepupgamerapp.data.repository.PaymentRepositoryImpl
import com.applevelup.levepupgamerapp.domain.usecase.AddPaymentMethodUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class AddPaymentUiState(
    val cardholderName: String = "",
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val isValid: Boolean = false,
    val showError: Boolean = false
)

class AddPaymentViewModel(
    private val repo: PaymentRepositoryImpl = PaymentRepositoryImpl()
) : ViewModel() {

    private val addMethodUseCase = AddPaymentMethodUseCase(repo)

    private val _uiState = MutableStateFlow(AddPaymentUiState())
    val uiState: StateFlow<AddPaymentUiState> = _uiState

    fun onNameChange(value: String) {
        _uiState.update { it.copy(cardholderName = value) }
        validateForm()
    }

    fun onNumberChange(value: String) {
        if (value.length <= 16 && value.all { it.isDigit() })
            _uiState.update { it.copy(cardNumber = value) }
        validateForm()
    }

    fun onExpiryChange(value: String) {
        if (value.length <= 4 && value.all { it.isDigit() })
            _uiState.update { it.copy(expiryDate = value) }
        validateForm()
    }

    fun onCvvChange(value: String) {
        if (value.length <= 3 && value.all { it.isDigit() })
            _uiState.update { it.copy(cvv = value) }
        validateForm()
    }

    private fun validateForm() {
        val s = _uiState.value
        val isValid = s.cardholderName.isNotBlank() &&
                s.cardNumber.length == 16 &&
                s.expiryDate.length == 4 &&
                s.cvv.length == 3
        _uiState.update { it.copy(isValid = isValid) }
    }

    fun saveCard(): Boolean {
        val s = _uiState.value
        if (!s.isValid) {
            _uiState.update { it.copy(showError = true) }
            return false
        }
        val method = addMethodUseCase(s.cardholderName, s.cardNumber, s.expiryDate)
        repo.setDefaultPaymentMethod(method.id)
        _uiState.value = AddPaymentUiState()
        return true
    }
}
