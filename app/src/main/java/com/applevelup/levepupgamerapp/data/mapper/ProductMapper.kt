package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.local.entity.ProductEntity
import com.applevelup.levepupgamerapp.domain.model.Product

object ProductMapper {

	fun toDomain(entity: ProductEntity): Product {
		return Product(
			id = entity.id,
			name = entity.name,
			price = entity.price,
			oldPrice = entity.oldPrice,
			rating = entity.rating,
			reviews = entity.reviews,
			imageRes = entity.imageRes,
			category = entity.category,
			description = entity.description.orEmpty()
		)
	}

		fun toEntity(product: Product): ProductEntity {
		return ProductEntity(
			id = product.id,
			name = product.name,
			price = product.price,
			oldPrice = product.oldPrice,
			rating = product.rating,
			reviews = product.reviews,
				imageRes = product.imageRes,
				category = product.category,
				description = product.description
		)
	}
}