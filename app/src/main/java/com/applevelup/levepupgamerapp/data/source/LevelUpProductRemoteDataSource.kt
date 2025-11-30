package com.applevelup.levepupgamerapp.data.source

import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.domain.model.Product
import kotlin.math.absoluteValue
import com.applevelup.levepupgamerapp.data.source.ProductRemoteDataSource

class LevelUpProductRemoteDataSource(
    private val api: LevelUpMobileApi,
    private val idProvider: (String) -> Int = { code -> code.hashCode().absoluteValue }
) : ProductRemoteDataSource {

    override suspend fun fetchProducts(): List<Product> {
        val payload = api.getProducts()
        return payload.map { dto ->
            Product(
                id = idProvider(dto.codigo),
                code = dto.codigo,
                name = dto.nombre,
                price = dto.precio,
                oldPrice = null,
                rating = 0f,
                reviews = 0,
                imageRes = null,
                imageUrl = dto.imagenUrl,
                imageUri = null,
                category = dto.categoria,
                description = dto.descripcion.orEmpty(),
                stock = dto.stock
            )
        }
    }
}
