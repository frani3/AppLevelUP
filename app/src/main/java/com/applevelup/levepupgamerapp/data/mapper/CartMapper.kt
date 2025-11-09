package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.local.dao.CartItemWithProduct
import com.applevelup.levepupgamerapp.data.local.entity.CartItemEntity
import com.applevelup.levepupgamerapp.domain.model.CartItem

object CartMapper {

	fun toDomain(relation: CartItemWithProduct): CartItem {
		return CartItem(
			id = relation.product.id,
			name = relation.product.name,
			price = relation.product.price,
			imageRes = relation.product.imageRes,
			quantity = relation.cart.quantity
		)
	}

	fun toEntity(cartItem: CartItem, userId: Long): CartItemEntity {
		return CartItemEntity(
			userId = userId,
			productId = cartItem.id,
			quantity = cartItem.quantity
		)
	}
}