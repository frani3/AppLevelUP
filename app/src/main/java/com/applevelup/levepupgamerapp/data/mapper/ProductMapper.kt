package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.local.entity.ProductEntity
import com.applevelup.levepupgamerapp.domain.model.Product

object ProductMapper {

	fun toDomain(entity: ProductEntity): Product {
		return Product(
			id = entity.id,
			code = entity.code,
			name = entity.name,
			price = entity.price,
			oldPrice = entity.oldPrice,
			rating = entity.rating,
			reviews = entity.reviews,
			imageRes = entity.imageRes,
			imageUrl = entity.imageUrl,
			imageUri = entity.imageUri,
			category = entity.category,
			description = entity.description.orEmpty(),
			stock = entity.stock
		)
	}

	fun toEntity(product: Product): ProductEntity {
		return ProductEntity(
			id = product.id,
			code = product.code,
			name = product.name,
			price = product.price,
			oldPrice = product.oldPrice,
			rating = product.rating,
			reviews = product.reviews,
			imageRes = product.imageRes,
			imageUrl = product.imageUrl,
			imageUri = product.imageUri,
			category = product.category,
			description = product.description,
			stock = product.stock
		)
	}
}