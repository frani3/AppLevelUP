package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.usecase.AddToCartUseCase
import com.applevelup.levepupgamerapp.domain.usecase.SearchProductsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val results: List<Product> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false,
    val errorMessage: String? = null
)

sealed interface SearchEvent {
    data object ItemAdded : SearchEvent
}

class SearchViewModel(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _events = MutableSharedFlow<SearchEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<SearchEvent> = _events.asSharedFlow()

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value, errorMessage = null) }
    }

    fun setInitialQuery(query: String) {
        if (query == _uiState.value.query) return
        _uiState.update { it.copy(query = query, errorMessage = null) }
        if (query.isNotBlank()) {
            search()
        } else {
            _uiState.update { it.copy(results = emptyList(), hasSearched = false) }
        }
    }

    fun search() {
        val currentQuery = _uiState.value.query.trim()
        if (currentQuery.isEmpty()) {
            _uiState.update {
                it.copy(
                    results = emptyList(),
                    hasSearched = true,
                    isSearching = false,
                    errorMessage = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, errorMessage = null) }
            try {
                val products = searchProductsUseCase(currentQuery)
                _uiState.update {
                    it.copy(
                        results = products,
                        hasSearched = true,
                        isSearching = false,
                        errorMessage = null
                    )
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isSearching = false,
                        errorMessage = "No pudimos realizar la búsqueda. Intenta nuevamente."
                    )
                }
            }
        }
    }

    fun addProductToCart(productId: Int) {
        viewModelScope.launch {
            addToCartUseCase(productId)
            _events.emit(SearchEvent.ItemAdded)
        }
    }
}
