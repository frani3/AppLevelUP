package com.applevelup.levepupgamerapp.domain.model.levelup

data class LevelUpUserProfile(
    val run: String,
    val name: String,
    val email: String,
    val address: String?,
    val commune: String?,
    val region: String?,
    val stats: LevelUpStats?,
    // Perfil: "Cliente", "Vendedor", "Administrador"
    val perfil: String? = null,
    // true si systemAccount es true en la API
    val isSuperAdmin: Boolean = false
)
