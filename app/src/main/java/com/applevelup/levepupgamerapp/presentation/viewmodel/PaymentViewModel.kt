package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.applevelup.levepupgamerapp.domain.model.CardType
import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class PaymentUiState(
    val methods: List<PaymentMethod> = emptyList(),
    val isLoading: Boolean = false,
    val infoMessage: String? = null
)

/**
 * PaymentViewModel simplificado.
 * La API de LevelUp no soporta métodos de pago personalizados.
 * Solo se muestran opciones predefinidas de pago.
 */
class PaymentViewModel : ViewModel() {

    // Métodos de pago disponibles (configuración fija - la API no maneja métodos de pago)
    private val availablePaymentMethods = listOf(
        PaymentMethod(id = 1, cardType = CardType.OTHER, cardBrand = "Tarjeta de Crédito/Débito", lastFourDigits = "****", isDefault = true),
        PaymentMethod(id = 2, cardType = CardType.TRANSFER, cardBrand = "Transferencia Bancaria", lastFourDigits = "****", isDefault = false)
    )

    private val _uiState = MutableStateFlow(PaymentUiState(
        methods = availablePaymentMethods,
        isLoading = false,
        infoMessage = "Los métodos de pago se procesan de forma segura al momento de la compra"
    ))
    val uiState: StateFlow<PaymentUiState> = _uiState

    fun loadMethods() {
        // Los métodos son estáticos, no requiere carga
        _uiState.update { it.copy(methods = availablePaymentMethods, isLoading = false) }
    }

    fun deleteMethod(id: Int) {
        // No se pueden eliminar métodos predefinidos
        _uiState.update { it.copy(infoMessage = "Este método de pago no puede ser eliminado") }
    }

    fun markAsDefault(id: Int) {
        val updatedMethods = availablePaymentMethods.map { method ->
            method.copy(isDefault = method.id == id)
        }
        _uiState.update { it.copy(methods = updatedMethods) }
    }
    
    fun clearMessage() {
        _uiState.update { it.copy(infoMessage = null) }
    }
}
