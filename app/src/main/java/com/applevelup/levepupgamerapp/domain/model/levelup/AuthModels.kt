package com.applevelup.levepupgamerapp.domain.model.levelup

data class LoginCredentials(
    val correo: String,
    val password: String
)

data class RegistrationData(
    val run: String,
    val nombre: String,
    val correo: String,
    val password: String,
    val direccion: String,
    val region: String,
    val comuna: String,
    val referralCode: String?
)

data class AddressInput(
    val alias: String,
    val direccion: String,
    val numero: String? = null,
    val comuna: String,
    val region: String,
    val isPrimary: Boolean = false,
    val complement: String? = null
)
