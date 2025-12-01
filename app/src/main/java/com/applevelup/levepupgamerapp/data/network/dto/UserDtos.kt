package com.applevelup.levepupgamerapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    val run: String,
    val nombre: String,
    val correo: String,
    val direccion: String?,
    val comuna: String?,
    val region: String?,
    val levelUpStats: LevelUpStatsDto?,
    // Campo de perfil: "Cliente", "Vendedor", "Administrador"
    val perfil: String? = null,
    // true si es superadmin
    val systemAccount: Boolean? = null
)

data class UpdateProfileRequestDto(
    val nombre: String,
    val correo: String,
    val password: String? = null
)

data class LevelUpStatsDto(
    val points: Int,
    val exp: LevelUpExperienceDto,
    val referralCode: String?,
    val updatedAt: String
)

@Suppress("SpellCheckingInspection")
data class LevelUpExperienceDto(
    val compras: Int,
    val torneos: Int,
    val referidos: Int
)
