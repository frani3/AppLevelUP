package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.local.dao.CartDao
import com.applevelup.levepupgamerapp.data.local.dao.ProductDao
import com.applevelup.levepupgamerapp.data.local.entity.CartItemEntity
import com.applevelup.levepupgamerapp.data.mapper.CartMapper
import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private val cartDao: CartDao = LevelUpApplication.database.cartDao(),
    private val productDao: ProductDao = LevelUpApplication.database.productDao()
) : CartRepository {

    override fun observeCartItems(): Flow<List<CartItem>> {
        return cartDao.observeCartItems().map { relations ->
            relations.map(CartMapper::toDomain)
        }
    }

    override suspend fun addProduct(productId: Int, quantity: Int) {
        productDao.getProductById(productId)
            ?: throw IllegalArgumentException("Producto inexistente con id=$productId")

        val current = cartDao.getByProductId(productId)
        val newQuantity = (current?.quantity ?: 0) + quantity
        cartDao.upsert(CartItemEntity(productId = productId, quantity = newQuantity))
    }

    override suspend fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteByProductId(productId)
            return
        }

        val current = cartDao.getByProductId(productId)
        if (current == null) {
            cartDao.upsert(CartItemEntity(productId = productId, quantity = quantity))
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    override suspend fun removeItem(productId: Int) {
        cartDao.deleteByProductId(productId)
    }

    override suspend fun clearCart() {
        cartDao.clear()
    }
}
