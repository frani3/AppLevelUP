package com.applevelup.levepupgamerapp.domain.model

data class Order(
    val id: String,
    val date: String,
    val status: String,
    val total: String,
    val itemCount: Int
)
