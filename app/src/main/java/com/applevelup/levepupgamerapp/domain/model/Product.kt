package com.applevelup.levepupgamerapp.domain.model

data class Product(
    val id: Int,
    val code: String,
    val name: String,
    val price: Double,
    val oldPrice: Double? = null,
    val rating: Float,
    val reviews: Int = 0,
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val imageUri: String? = null,
    val category: String,
    val description: String = "",
    val stock: Int = 0
)