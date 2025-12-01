package com.applevelup.levepupgamerapp.domain.model.levelup

data class LevelUpAddress(
    val id: String,
    val fullName: String,
    val line1: String,
    val city: String,
    val region: String,
    val country: String,
    val isPrimary: Boolean
)
