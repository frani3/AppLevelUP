package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.CartRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.SessionRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.repository.CartRepository
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import com.applevelup.levepupgamerapp.domain.usecase.ClearCartUseCase
import com.applevelup.levepupgamerapp.domain.usecase.GetCartUseCase
import com.applevelup.levepupgamerapp.domain.usecase.RemoveCartItemUseCase
import com.applevelup.levepupgamerapp.domain.usecase.UpdateCartItemQuantityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val shippingCost: Double = 0.0,
    val total: Double = 0.0
)

class CartViewModel(
    private val repository: CartRepository = CartRepositoryImpl(),
    private val sessionRepository: SessionRepository = SessionRepositoryImpl(),
    private val getCartUseCase: GetCartUseCase = GetCartUseCase(repository, sessionRepository),
    private val updateQuantityUseCase: UpdateCartItemQuantityUseCase = UpdateCartItemQuantityUseCase(repository, sessionRepository),
    private val removeItemUseCase: RemoveCartItemUseCase = RemoveCartItemUseCase(repository, sessionRepository),
    private val clearCartUseCase: ClearCartUseCase = ClearCartUseCase(repository, sessionRepository)
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            getCartUseCase().collectLatest { items ->
                calculateTotals(items)
            }
        }
    }

    private fun calculateTotals(items: List<CartItem>) {
        val subtotal = items.sumOf { it.price * it.quantity }
        val shipping = if (subtotal > 0) SHIPPING_FEE_CLP else 0.0
        val total = subtotal + shipping
        _uiState.update { CartUiState(items, subtotal, shipping, total) }
    }

    fun updateQuantity(itemId: Int, newQuantity: Int) {
        viewModelScope.launch {
            updateQuantityUseCase(itemId, newQuantity)
        }
    }

    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            removeItemUseCase(itemId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase()
        }
    }

    companion object {
        private const val SHIPPING_FEE_CLP = 5990.0
    }
}
