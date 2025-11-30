package com.applevelup.levepupgamerapp.data.network.dto

data class AddressDto(
    val id: String,
    val alias: String,
    val direccion: String,
    val numero: String?,
    val comuna: String,
    val region: String,
    val run: String,
    val esPrincipal: Boolean
)

data class AddressRequestDto(
    val alias: String,
    val direccion: String,
    val numero: String? = null,
    val comuna: String,
    val region: String,
    val esPrincipal: Boolean = false
)
