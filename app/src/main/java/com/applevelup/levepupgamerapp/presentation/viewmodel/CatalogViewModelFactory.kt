package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applevelup.levepupgamerapp.data.repository.CartRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.ProductRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.SessionRepositoryImpl
import com.applevelup.levepupgamerapp.domain.usecase.AddToCartUseCase
import com.applevelup.levepupgamerapp.domain.usecase.GetCatalogProductsUseCase

class CatalogViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val productRepository = ProductRepositoryImpl()
        val getCatalogProducts = GetCatalogProductsUseCase(productRepository)
        val addToCart = AddToCartUseCase(CartRepositoryImpl(), SessionRepositoryImpl())
        return CatalogViewModel(getCatalogProducts, addToCart) as T
    }
}
