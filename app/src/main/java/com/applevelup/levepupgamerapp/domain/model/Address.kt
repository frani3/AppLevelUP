package com.applevelup.levepupgamerapp.domain.model

data class Address(
    val id: Int,
    val alias: String,
    val street: String,
    val city: String,
    val details: String,
    val isDefault: Boolean
)
