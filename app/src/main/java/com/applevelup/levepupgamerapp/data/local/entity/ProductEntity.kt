package com.applevelup.levepupgamerapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
	@PrimaryKey val id: Int,
	val code: String,
	val name: String,
	val price: Double,
	@ColumnInfo(name = "old_price") val oldPrice: Double?,
	val rating: Float,
	val reviews: Int,
	@ColumnInfo(name = "image_res") val imageRes: Int?,
	@ColumnInfo(name = "image_url") val imageUrl: String?,
	@ColumnInfo(name = "image_uri") val imageUri: String?,
	val category: String,
	val description: String? = null,
	@ColumnInfo(name = "stock") val stock: Int
)