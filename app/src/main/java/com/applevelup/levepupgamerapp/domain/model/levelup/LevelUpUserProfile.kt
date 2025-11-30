package com.applevelup.levepupgamerapp.domain.model.levelup

data class LevelUpUserProfile(
    val run: String,
    val name: String,
    val email: String,
    val address: String?,
    val commune: String?,
    val region: String?,
    val stats: LevelUpStats?
)
