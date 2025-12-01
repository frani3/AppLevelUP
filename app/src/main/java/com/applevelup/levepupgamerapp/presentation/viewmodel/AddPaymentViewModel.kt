package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class AddPaymentUiState(
    val cardholderName: String = "",
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val isValid: Boolean = false,
    val showError: Boolean = false,
    val infoMessage: String? = null
)

/**
 * AddPaymentViewModel simplificado.
 * La API de LevelUp no soporta métodos de pago personalizados.
 * Este ViewModel se mantiene por compatibilidad pero no guarda tarjetas.
 * Los pagos se procesan directamente en el checkout.
 */
class AddPaymentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddPaymentUiState(
        infoMessage = "Los pagos con tarjeta se procesan de forma segura al momento de la compra. No almacenamos datos de tarjetas."
    ))
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

    suspend fun saveCard(): Boolean {
        // La API no soporta guardar métodos de pago
        // Informamos al usuario que los pagos se procesan en el checkout
        _uiState.update { 
            it.copy(
                infoMessage = "Los pagos se procesan al momento de la compra. Tu tarjeta no será almacenada.",
                showError = false
            ) 
        }
        // Retornamos true para permitir navegar de vuelta
        return true
    }
    
    fun clearMessage() {
        _uiState.update { it.copy(infoMessage = null) }
    }
}
