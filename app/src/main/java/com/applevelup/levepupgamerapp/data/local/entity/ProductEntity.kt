package com.applevelup.levepupgamerapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
	@PrimaryKey val id: Int,
	val name: String,
	val price: Double,
	@ColumnInfo(name = "old_price") val oldPrice: Double?,
	val rating: Float,
	val reviews: Int,
	@ColumnInfo(name = "image_res") val imageRes: Int,
	val category: String,
	val description: String? = null
)