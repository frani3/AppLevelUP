package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.ProductRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.usecase.GetProductByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductDetailUiState(
    val product: Product? = null,
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val totalPrice: Double get() = (product?.price ?: 0.0) * quantity
}

class ProductDetailViewModel(
    private val getProductById: GetProductByIdUseCase = GetProductByIdUseCase(ProductRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            try {
                val result = getProductById(productId)
                _uiState.update {
                    it.copy(product = result, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }
    }

    fun increaseQuantity() {
        _uiState.update { it.copy(quantity = it.quantity + 1) }
    }

    fun decreaseQuantity() {
        _uiState.update {
            if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else it
        }
    }

    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }
}
