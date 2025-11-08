package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.LandingRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LandingUiState(
    val promotions: List<Promotion> = emptyList(),
    val categories: List<Category> = emptyList(),
    val featured: List<ProductSummary> = emptyList(),
    val newProducts: List<ProductSummary> = emptyList(),
    val currentPage: Int = 0
)

class LandingViewModel(
    private val repo: LandingRepositoryImpl = LandingRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState

    init {
        loadLandingContent()
        autoScrollCarousel()
    }

    private fun loadLandingContent() {
        _uiState.update {
            it.copy(
                promotions = repo.getPromotions(),
                categories = repo.getCategories(),
                featured = repo.getFeaturedProducts(),
                newProducts = repo.getNewProducts()
            )
        }
    }

    private fun autoScrollCarousel() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                _uiState.update {
                    val next = (it.currentPage + 1) % it.promotions.size
                    it.copy(currentPage = next)
                }
            }
        }
    }
}
