package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.Product

interface ProductRepository {
    fun getProductsByCategory(categoryName: String): List<Product>
}
