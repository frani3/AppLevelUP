package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applevelup.levepupgamerapp.data.repository.ProductRepositoryImpl
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.usecase.SearchProductsUseCase

class SearchViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val productRepository = ProductRepositoryImpl()
        val searchProductsUseCase = SearchProductsUseCase(productRepository)
        val cartRepository = LevelUpDependencyContainer.cartRepository
        return SearchViewModel(searchProductsUseCase, cartRepository) as T
    }
}
