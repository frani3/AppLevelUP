package com.applevelup.levepupgamerapp.domain.model

data class User(
	val id: Long,
	val fullName: String,
	val email: String,
	val isSuperAdmin: Boolean,
	val avatarRes: Int?
)