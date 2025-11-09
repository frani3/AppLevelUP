package com.applevelup.levepupgamerapp.domain.model

data class ProductReview(
    val id: String,
    val author: String,
    val rating: Float,
    val comment: String,
    val date: String
)
