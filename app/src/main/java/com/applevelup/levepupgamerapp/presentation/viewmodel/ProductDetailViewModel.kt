package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.FavoritesRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.ProductReviewRepositoryImpl
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.model.ProductReview
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpProduct
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpCartRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpProductRepository
import com.applevelup.levepupgamerapp.domain.usecase.GetProductReviewsUseCase
import com.applevelup.levepupgamerapp.domain.usecase.ObserveFavoriteProductIdsUseCase
import com.applevelup.levepupgamerapp.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductDetailUiState(
    val product: Product? = null,
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val addedToCart: Boolean = false,
    val reviews: List<ProductReview> = emptyList()
) {
    val totalPrice: Double get() = (product?.price ?: 0.0) * quantity
}

class ProductDetailViewModel(
    private val productRepository: LevelUpProductRepository = LevelUpDependencyContainer.productRepository,
    private val cartRepository: LevelUpCartRepository = LevelUpDependencyContainer.cartRepository,
    private val getProductReviews: GetProductReviewsUseCase = GetProductReviewsUseCase(ProductReviewRepositoryImpl()),
    private val observeFavoriteIds: ObserveFavoriteProductIdsUseCase = ObserveFavoriteProductIdsUseCase(FavoritesRepositoryImpl()),
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = ToggleFavoriteUseCase(FavoritesRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState
    private var favoritesJob: Job? = null

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            try {
                // Buscar producto en el cache de LevelUp por ID (hash del código)
                val products = productRepository.observeProducts().first()
                val levelUpProduct = products.find { it.id == productId }
                
                val product = levelUpProduct?.toProduct()
                val reviews = product?.let { getProductReviews(productId) }.orEmpty()
                
                _uiState.update {
                    it.copy(product = product, isLoading = false, reviews = reviews)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message, isLoading = false)
                }
            }
        }

        favoritesJob?.cancel()
        favoritesJob = viewModelScope.launch {
            observeFavoriteIds().collectLatest { favoriteIds ->
                _uiState.update { state ->
                    state.copy(isFavorite = favoriteIds.contains(productId))
                }
            }
        }
    }
    
    /**
     * Convierte LevelUpProduct a Product para compatibilidad con UI existente
     */
    private fun LevelUpProduct.toProduct(): Product = Product(
        id = this.id,
        code = this.code,
        name = this.name,
        price = this.price,
        oldPrice = null,
        rating = 4.5f, // Default rating
        reviews = 0,
        imageRes = null,
        imageUrl = this.imageUrl,
        imageUri = null,
        category = this.category,
        description = this.description ?: "",
        stock = this.stock
    )

    fun increaseQuantity() {
        _uiState.update { it.copy(quantity = it.quantity + 1) }
    }

    fun decreaseQuantity() {
        _uiState.update {
            if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else it
        }
    }

    fun toggleFavorite() {
        val productId = _uiState.value.product?.id ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(productId)
        }
    }

    fun addCurrentSelectionToCart() {
        val product = _uiState.value.product ?: return
        val quantity = _uiState.value.quantity

        viewModelScope.launch {
            when (val result = cartRepository.addItem(product.code, quantity)) {
                is LevelUpResult.Success -> _uiState.update { it.copy(addedToCart = true) }
                is LevelUpResult.Failure -> _uiState.update { it.copy(error = result.throwable.message) }
            }
        }
    }

    fun consumeAddedToCartFlag() {
        if (_uiState.value.addedToCart) {
            _uiState.update { it.copy(addedToCart = false) }
        }
    }
}
