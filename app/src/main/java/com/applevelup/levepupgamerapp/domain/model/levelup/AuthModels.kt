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
    val fullName: String,
    val line1: String,
    val city: String,
    val region: String,
    val country: String = "Chile",
    val isPrimary: Boolean = false
)
