package com.applevelup.levepupgamerapp.presentation.viewmodel.levelup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpProduct
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCategory
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchCategoriesUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchProductsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LevelUpCatalogViewModel(
    private val fetchProductsUseCase: FetchProductsUseCase,
    private val fetchCategoriesUseCase: FetchCategoriesUseCase
) : ViewModel() {

    val products = fetchProductsUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, LevelUpResource.Loading)

    val categories = fetchCategoriesUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, LevelUpResource.Loading)

    fun refreshProducts(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            fetchProductsUseCase(forceRefresh).first()
        }
    }

    fun refreshCategories(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            fetchCategoriesUseCase(forceRefresh).first()
        }
    }
}
