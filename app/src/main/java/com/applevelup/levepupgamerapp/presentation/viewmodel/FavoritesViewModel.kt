package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.FavoritesRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.ProductRepositoryImpl
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpCartRepository
import com.applevelup.levepupgamerapp.domain.usecase.ObserveFavoriteProductsUseCase
import com.applevelup.levepupgamerapp.domain.usecase.ToggleFavoriteUseCase
import com.applevelup.levepupgamerapp.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favorites: List<Product> = emptyList(),
    val errorMessage: String? = null
)

class FavoritesViewModel(
    favoritesRepository: FavoritesRepository = FavoritesRepositoryImpl(),
    private val observeFavoriteProducts: ObserveFavoriteProductsUseCase = ObserveFavoriteProductsUseCase(
        favoritesRepository,
        ProductRepositoryImpl()
    ),
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = ToggleFavoriteUseCase(favoritesRepository),
    private val cartRepository: LevelUpCartRepository = LevelUpDependencyContainer.cartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState

    private val _events = MutableSharedFlow<FavoritesEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<FavoritesEvent> = _events

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            observeFavoriteProducts()
                .onStart { _uiState.update { it.copy(isLoading = true, errorMessage = null) } }
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "No pudimos cargar tus favoritos")
                    }
                }
                .collect { products ->
                    _uiState.update {
                        it.copy(isLoading = false, favorites = products, errorMessage = null)
                    }
                }
        }
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(productId)
        }
    }

    fun addProductToCart(productCode: String) {
        viewModelScope.launch {
            when (val result = cartRepository.addItem(productCode, 1)) {
                is LevelUpResult.Success -> _events.emit(FavoritesEvent.ItemAddedToCart)
                is LevelUpResult.Failure -> _uiState.update { it.copy(errorMessage = result.throwable.message) }
            }
        }
    }
    
    // Sobrecarga para compatibilidad con código existente que usa productId
    fun addProductToCart(productId: Int) {
        val product = _uiState.value.favorites.find { it.id == productId }
        if (product != null) {
            addProductToCart(product.code)
        }
    }
}

sealed class FavoritesEvent {
    object ItemAddedToCart : FavoritesEvent()
}
