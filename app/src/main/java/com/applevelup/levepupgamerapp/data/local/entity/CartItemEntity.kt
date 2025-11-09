package com.applevelup.levepupgamerapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
	tableName = "cart_items",
	foreignKeys = [
		ForeignKey(
			entity = ProductEntity::class,
			parentColumns = ["id"],
			childColumns = ["product_id"],
			onDelete = ForeignKey.CASCADE
		)
	],
	indices = [Index(value = ["product_id"])],
	primaryKeys = ["product_id"]
)
data class CartItemEntity(
	@ColumnInfo(name = "product_id") val productId: Int,
	val quantity: Int
)