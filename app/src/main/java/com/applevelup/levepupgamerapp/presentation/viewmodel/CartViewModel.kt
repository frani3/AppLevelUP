package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCart
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpCartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val shippingCost: Double = 0.0,
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class CartViewModel(
    private val cartRepository: LevelUpCartRepository = LevelUpDependencyContainer.cartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    init {
        observeCart()
        refreshCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.observeCart().collectLatest { cart ->
                calculateTotals(cart)
            }
        }
    }

    private fun refreshCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = cartRepository.refreshCart()) {
                is LevelUpResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                is LevelUpResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, error = result.throwable.message) }
                }
            }
        }
    }

    private fun calculateTotals(cart: LevelUpCart) {
        val items = cart.items.map { item ->
            CartItem(
                id = item.productCode.hashCode(), // Para compatibilidad con UI existente
                name = item.name,
                price = item.unitPrice,
                imageUrl = item.imageUrl,
                quantity = item.quantity
            )
        }
        val subtotal = cart.subtotal
        val shipping = if (subtotal > 0) SHIPPING_FEE_CLP else 0.0
        val total = subtotal + shipping
        _uiState.update { it.copy(items = items, subtotal = subtotal, shippingCost = shipping, total = total) }
    }

    fun updateQuantity(productCode: String, newQuantity: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = cartRepository.updateItemQuantity(productCode, newQuantity)) {
                is LevelUpResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                is LevelUpResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, error = result.throwable.message) }
                }
            }
        }
    }

    // Sobrecarga para compatibilidad con código existente que usa itemId
    fun updateQuantity(itemId: Int, newQuantity: Int) {
        val item = _uiState.value.items.find { it.id == itemId }
        if (item != null) {
            // Buscar el productCode correspondiente del item actual
            viewModelScope.launch {
                cartRepository.observeCart().collectLatest { cart ->
                    val cartItem = cart.items.find { it.productCode.hashCode() == itemId }
                    if (cartItem != null) {
                        updateQuantity(cartItem.productCode, newQuantity)
                    }
                    return@collectLatest
                }
            }
        }
    }

    fun removeItem(productCode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = cartRepository.removeItem(productCode)) {
                is LevelUpResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                is LevelUpResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, error = result.throwable.message) }
                }
            }
        }
    }

    // Sobrecarga para compatibilidad con código existente que usa itemId
    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            cartRepository.observeCart().collectLatest { cart ->
                val cartItem = cart.items.find { it.productCode.hashCode() == itemId }
                if (cartItem != null) {
                    removeItem(cartItem.productCode)
                }
                return@collectLatest
            }
        }
    }

    fun addItem(productCode: String, quantity: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = cartRepository.addItem(productCode, quantity)) {
                is LevelUpResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                is LevelUpResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, error = result.throwable.message) }
                }
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = cartRepository.clearCart()) {
                is LevelUpResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                is LevelUpResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, error = result.throwable.message) }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    companion object {
        private const val SHIPPING_FEE_CLP = 5990.0
    }
}
