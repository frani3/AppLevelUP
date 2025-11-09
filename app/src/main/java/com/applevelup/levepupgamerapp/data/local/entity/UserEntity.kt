package com.applevelup.levepupgamerapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "users",
	indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	@ColumnInfo(name = "full_name") val fullName: String,
	val email: String,
	@ColumnInfo(name = "password_hash") val passwordHash: String,
	@ColumnInfo(name = "avatar_res") val avatarRes: Int?,
	@ColumnInfo(name = "order_count") val orderCount: Int,
	@ColumnInfo(name = "wishlist_count") val wishlistCount: Int,
	@ColumnInfo(name = "coupon_count") val couponCount: Int,
	@ColumnInfo(name = "is_super_admin") val isSuperAdmin: Boolean,
	@ColumnInfo(name = "photo_uri") val photoUri: String? = null
)