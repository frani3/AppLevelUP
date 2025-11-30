package com.applevelup.levepupgamerapp.domain.model.levelup

data class LevelUpAddress(
    val id: String,
    val run: String,
    val alias: String,
    val street: String,
    val numero: String?,
    val comuna: String,
    val region: String,
    val complement: String?,
    val isPrimary: Boolean
)
