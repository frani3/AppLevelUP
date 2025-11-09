package com.applevelup.levepupgamerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.applevelup.levepupgamerapp.data.local.entity.CartItemEntity
import com.applevelup.levepupgamerapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

	@Transaction
	@Query("SELECT * FROM cart_items WHERE user_id = :userId")
	fun observeCartItems(userId: Long): Flow<List<CartItemWithProduct>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(cartItem: CartItemEntity)

	@Query("UPDATE cart_items SET quantity = :quantity WHERE user_id = :userId AND product_id = :productId")
	suspend fun updateQuantity(userId: Long, productId: Int, quantity: Int)

	@Query("DELETE FROM cart_items WHERE user_id = :userId AND product_id = :productId")
	suspend fun deleteByProductId(userId: Long, productId: Int)

	@Query("DELETE FROM cart_items WHERE user_id = :userId")
	suspend fun clear(userId: Long)

	@Query("SELECT * FROM cart_items WHERE user_id = :userId AND product_id = :productId LIMIT 1")
	suspend fun getByProductId(userId: Long, productId: Int): CartItemEntity?
}

data class CartItemWithProduct(
	@Embedded val cart: CartItemEntity,
	@Relation(
		parentColumn = "product_id",
		entityColumn = "id"
	)
	val product: ProductEntity
)