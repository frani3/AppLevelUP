package com.applevelup.levepupgamerapp.domain.model

data class User(
	val id: Long,
	val fullName: String,
	val email: String,
	val isSuperAdmin: Boolean,
	val avatarRes: Int?,
	val run: String? = null,
	val firstName: String? = null,
	val lastName: String? = null,
	val profileRole: String? = null,
	val hasPassword: Boolean = true,
	val hasLifetimeDiscount: Boolean = false,
	val isSystem: Boolean = false
)