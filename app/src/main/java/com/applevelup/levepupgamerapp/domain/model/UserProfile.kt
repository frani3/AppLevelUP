package com.applevelup.levepupgamerapp.domain.model

data class UserProfile(
    val name: String,
    val email: String,
    val avatarRes: Int,
    val photoUri: String?,
    val orderCount: Int,
    val wishlistCount: Int,
    val couponCount: Int,
    val run: String? = null,
    val profileRole: String? = null,
    val birthDate: String? = null,
    val region: String? = null,
    val comuna: String? = null,
    val address: String? = null,
    val hasLifetimeDiscount: Boolean = false,
    val isSystem: Boolean = false
)
