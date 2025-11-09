package com.applevelup.levepupgamerapp.domain.model

data class UserProfile(
    val name: String,
    val email: String,
    val avatarRes: Int,
    val photoUri: String?,
    val orderCount: Int,
    val wishlistCount: Int,
    val couponCount: Int
)
