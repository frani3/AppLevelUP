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
	@ColumnInfo(name = "run") val run: String? = null,
	@ColumnInfo(name = "full_name") val fullName: String,
	@ColumnInfo(name = "first_name") val firstName: String? = null,
	@ColumnInfo(name = "last_name") val lastName: String? = null,
	val email: String,
	@ColumnInfo(name = "password_hash") val passwordHash: String? = null,
	@ColumnInfo(name = "avatar_res") val avatarRes: Int? = null,
	@ColumnInfo(name = "order_count") val orderCount: Int = 0,
	@ColumnInfo(name = "wishlist_count") val wishlistCount: Int = 0,
	@ColumnInfo(name = "coupon_count") val couponCount: Int = 0,
	@ColumnInfo(name = "profile_role") val profileRole: String? = null,
	@ColumnInfo(name = "birth_date") val birthDate: String? = null,
	@ColumnInfo(name = "region") val region: String? = null,
	@ColumnInfo(name = "comuna") val comuna: String? = null,
	@ColumnInfo(name = "address") val address: String? = null,
	@ColumnInfo(name = "lifetime_discount") val hasLifetimeDiscount: Boolean = false,
	@ColumnInfo(name = "is_super_admin") val isSuperAdmin: Boolean,
	@ColumnInfo(name = "is_system") val isSystem: Boolean = false,
	@ColumnInfo(name = "photo_uri") val photoUri: String? = null
)