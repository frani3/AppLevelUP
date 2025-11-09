package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.model.ProductFilters
import com.applevelup.levepupgamerapp.domain.usecase.AddToCartUseCase
import com.applevelup.levepupgamerapp.domain.usecase.GetProductsByCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductListUiState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null,
    val filters: ProductFilters = ProductFilters(),
    val availablePriceRange: ClosedFloatingPointRange<Double> = 0.0..0.0
)

class ProductListViewModel(
    private val getProductsByCategory: GetProductsByCategoryUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState

    private val _events = MutableSharedFlow<ProductListEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private var currentCategory: String? = null
    private var currentFilters: ProductFilters = ProductFilters()
    private var baseProducts: List<Product> = emptyList()

    fun loadProducts(categoryName: String) {
        currentCategory = categoryName
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val baseFilters = ProductFilters(categories = setOf(categoryName))
                val result = getProductsByCategory(categoryName, baseFilters)
                baseProducts = result
                currentFilters = baseFilters
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = result,
                        filters = baseFilters,
                        errorMessage = null,
                        availablePriceRange = calculatePriceRange(result)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "No pudimos cargar los productos"
                    )
                }
            }
        }
    }

    fun applyFilters(filters: ProductFilters) {
        val category = currentCategory ?: return
        currentFilters = filters.copy(categories = setOf(category))
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = getProductsByCategory(category, currentFilters)
                if (currentFilters.activeFiltersCount() == 0) {
                    baseProducts = result
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = result,
                        filters = currentFilters,
                        availablePriceRange = calculatePriceRange(baseProducts)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "No pudimos aplicar los filtros"
                    )
                }
            }
        }
    }

    fun resetFilters() {
        val category = currentCategory ?: return
        applyFilters(ProductFilters(categories = setOf(category)))
    }

    fun addProductToCart(productId: Int) {
        viewModelScope.launch {
            addToCartUseCase(productId)
            _events.emit(ProductListEvent.ItemAdded)
        }
    }

    private fun calculatePriceRange(products: List<Product>): ClosedFloatingPointRange<Double> {
        if (products.isEmpty()) return 0.0..0.0
        val minPrice = products.minOf { it.price }
        val maxPrice = products.maxOf { it.price }
        return minPrice..maxPrice
    }
}

sealed class ProductListEvent {
    object ItemAdded : ProductListEvent()
}
