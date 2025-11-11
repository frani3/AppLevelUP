package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.model.ProductFilters
import com.applevelup.levepupgamerapp.domain.usecase.AddToCartUseCase
import com.applevelup.levepupgamerapp.domain.usecase.GetCatalogProductsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CatalogUiState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val filters: ProductFilters = ProductFilters(),
    val availableCategories: List<String> = emptyList(),
    val availablePriceRange: ClosedFloatingPointRange<Double> = 0.0..0.0,
    val errorMessage: String? = null
)

class CatalogViewModel(
    private val getCatalogProducts: GetCatalogProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState

    private val _events = MutableSharedFlow<ProductListEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private var baseProducts: List<Product> = emptyList()
    private var catalogCategories: Set<String> = emptySet()
    private var currentFilters: ProductFilters = ProductFilters()

    init {
        loadCatalog()
    }

    private fun loadCatalog() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                baseProducts = getCatalogProducts()
                catalogCategories = baseProducts.map { it.category }.toSet()
                currentFilters = ProductFilters()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = baseProducts,
                        filters = currentFilters,
                        availableCategories = catalogCategories.toList().sorted(),
                        availablePriceRange = calculatePriceRange(baseProducts),
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "No pudimos cargar el catálogo"
                    )
                }
            }
        }
    }

    fun applyFilters(filters: ProductFilters) {
        viewModelScope.launch {
            val sanitizedCategories = filters.categories
                .intersect(catalogCategories)
            val effectiveCategories = when {
                sanitizedCategories.isEmpty() -> emptySet()
                sanitizedCategories.size == catalogCategories.size -> emptySet()
                else -> sanitizedCategories
            }
            val sanitizedFilters = filters.copy(categories = effectiveCategories)
            currentFilters = sanitizedFilters
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = getCatalogProducts(sanitizedFilters)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = result,
                        filters = sanitizedFilters,
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
        applyFilters(ProductFilters())
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
