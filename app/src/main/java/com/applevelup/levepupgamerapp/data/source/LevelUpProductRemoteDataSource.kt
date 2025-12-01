package com.applevelup.levepupgamerapp.data.source

import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.data.network.dto.CreateProductRequestDto
import com.applevelup.levepupgamerapp.data.network.dto.ProductDto
import com.applevelup.levepupgamerapp.domain.model.Product
import kotlin.math.absoluteValue

class LevelUpProductRemoteDataSource(
    private val api: LevelUpMobileApi,
    private val idProvider: (String) -> Int = { code -> code.hashCode().absoluteValue }
) : ProductRemoteDataSource {

    override suspend fun fetchProducts(): List<Product> {
        val payload = api.getProducts()
        return payload.map { dto -> dto.toDomain() }
    }

    override suspend fun createProduct(product: Product): Product {
        val request = CreateProductRequestDto(
            codigo = product.code.takeIf { it.isNotBlank() },
            nombre = product.name,
            descripcion = product.description.takeIf { it.isNotBlank() },
            categoria = product.category,
            fabricante = null,
            distribuidor = null,
            precio = product.price,
            stock = product.stock.takeIf { it > 0 },
            stockCritico = null,
            imagenUrl = product.imageUrl?.takeIf { it.isNotBlank() }
        )
        val created = api.createProduct(request)
        return created.toDomain()
    }

    private fun ProductDto.toDomain(): Product {
        return Product(
            id = idProvider(codigo),
            code = codigo,
            name = nombre,
            price = precio,
            oldPrice = null,
            rating = 0f,
            reviews = 0,
            imageRes = null,
            imageUrl = imagenUrl,
            imageUri = null,
            category = categoria,
            description = descripcion.orEmpty(),
            stock = stock
        )
    }
}
