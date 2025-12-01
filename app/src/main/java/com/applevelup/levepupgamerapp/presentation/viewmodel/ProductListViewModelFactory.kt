package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applevelup.levepupgamerapp.data.repository.ProductRepositoryImpl
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.usecase.GetProductsByCategoryUseCase

class ProductListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = ProductRepositoryImpl()
        val getProducts = GetProductsByCategoryUseCase(repo)
        val cartRepository = LevelUpDependencyContainer.cartRepository
        return ProductListViewModel(getProducts, cartRepository) as T
    }
}
