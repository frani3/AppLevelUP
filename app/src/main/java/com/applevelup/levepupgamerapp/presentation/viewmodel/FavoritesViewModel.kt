package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.CartRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.FavoritesRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.ProductRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.SessionRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.usecase.AddToCartUseCase
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
    private val addToCartUseCase: AddToCartUseCase = AddToCartUseCase(
        CartRepositoryImpl(),
        SessionRepositoryImpl()
    )
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

    fun addProductToCart(productId: Int) {
        viewModelScope.launch {
            addToCartUseCase(productId)
            _events.emit(FavoritesEvent.ItemAddedToCart)
        }
    }
}

sealed class FavoritesEvent {
    object ItemAddedToCart : FavoritesEvent()
}
