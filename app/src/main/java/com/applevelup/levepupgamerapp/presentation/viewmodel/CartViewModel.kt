package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.CartRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val shippingCost: Double = 0.0,
    val total: Double = 0.0
)

class CartViewModel(
    private val repository: CartRepositoryImpl = CartRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            val items = repository.getCartItems()
            calculateTotals(items)
        }
    }

    private fun calculateTotals(items: List<CartItem>) {
        val subtotal = items.sumOf { it.price * it.quantity }
        val shipping = if (subtotal > 0) 5.99 else 0.0
        val total = subtotal + shipping
        _uiState.update { CartUiState(items, subtotal, shipping, total) }
    }

    fun updateQuantity(itemId: Int, newQuantity: Int) {
        repository.updateQuantity(itemId, newQuantity)
        loadCart()
    }

    fun removeItem(itemId: Int) {
        repository.removeItem(itemId)
        loadCart()
    }

    fun clearCart() {
        repository.clearCart()
        loadCart()
    }
}
