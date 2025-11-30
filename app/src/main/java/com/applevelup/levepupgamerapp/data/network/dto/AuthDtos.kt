package com.applevelup.levepupgamerapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    val correo: String,
    val password: String
)

@Suppress("Unused")
data class RegisterRequestDto(
    val run: String,
    val nombre: String,
    val correo: String,
    val password: String,
    val direccion: String,
    val region: String,
    val comuna: String,
    @SerializedName("codigoReferido")
    val referralCode: String? = null
)

data class AuthResponseDto(
    val token: String,
    val tokenType: String,
    val user: UserProfileDto
)
