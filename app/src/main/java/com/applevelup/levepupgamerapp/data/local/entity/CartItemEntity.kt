package com.applevelup.levepupgamerapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity

@Entity(
	tableName = "cart_items",
	foreignKeys = [
		ForeignKey(
			entity = ProductEntity::class,
			parentColumns = ["id"],
			childColumns = ["product_id"],
			onDelete = ForeignKey.CASCADE
		),
		ForeignKey(
			entity = UserEntity::class,
			parentColumns = ["id"],
			childColumns = ["user_id"],
			onDelete = ForeignKey.CASCADE
		)
	],
	indices = [Index(value = ["product_id"]), Index(value = ["user_id"])],
	primaryKeys = ["user_id", "product_id"]
)
data class CartItemEntity(
	@ColumnInfo(name = "user_id") val userId: Long,
	@ColumnInfo(name = "product_id") val productId: Int,
	val quantity: Int
)