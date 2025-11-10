package com.applevelup.levepupgamerapp.domain.model

data class ProductSummary(
    val id: Int,
    val name: String,
    val price: String,
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val imageUri: String? = null
)
