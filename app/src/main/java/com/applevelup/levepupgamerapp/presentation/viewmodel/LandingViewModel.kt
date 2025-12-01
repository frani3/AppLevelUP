package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.data.repository.LandingRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.*
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class LandingUiState(
    val promotions: List<Promotion> = emptyList(),
    val categories: List<Category> = emptyList(),
    val featured: List<ProductSummary> = emptyList(),
    val newProducts: List<ProductSummary> = emptyList(),
    val currentPage: Int = 0
)

class LandingViewModel(
    private val repo: LandingRepositoryImpl = LandingRepositoryImpl(),
    private val authRepository: LevelUpAuthRepository = LevelUpDependencyContainer.authRepository,
    private val productRepository: LevelUpProductRepository = LevelUpDependencyContainer.productRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState

    init {
        loadStaticContent()
        observeProducts()
        refreshProducts()
        autoScrollCarousel()
    }

    private fun loadStaticContent() {
        _uiState.update {
            it.copy(
                promotions = repo.getPromotions(),
                categories = repo.getCategories()
            )
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            productRepository.observeProducts().collectLatest { products ->
                val summaries = products.map { product ->
                    ProductSummary(
                        id = product.id,
                        name = product.name,
                        price = formatPrice(product.price),
                        imageUrl = product.imageUrl
                    )
                }
                // Dividir productos: primeros 4 como featured, siguientes como nuevos
                val featured = summaries.take(4)
                val newProducts = summaries.drop(4).take(4)
                
                _uiState.update {
                    it.copy(
                        featured = featured,
                        newProducts = newProducts
                    )
                }
            }
        }
    }

    private fun refreshProducts() {
        viewModelScope.launch {
            productRepository.refreshProducts(force = false)
        }
    }

    private fun autoScrollCarousel() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                _uiState.update { current ->
                    if (current.promotions.isEmpty()) {
                        current
                    } else {
                        val next = (current.currentPage + 1) % current.promotions.size
                        current.copy(currentPage = next)
                    }
                }
            }
        }
    }

    private fun formatPrice(price: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        return format.format(price).replace("CLP", "$").trim()
    }

    suspend fun logout() {
        authRepository.logout()
    }
}
