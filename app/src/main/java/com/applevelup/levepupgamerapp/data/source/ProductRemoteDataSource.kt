package com.applevelup.levepupgamerapp.data.source

import com.applevelup.levepupgamerapp.domain.model.Product

interface ProductRemoteDataSource {
    suspend fun fetchProducts(): List<Product>
}
