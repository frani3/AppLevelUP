package com.applevelup.levepupgamerapp.data.local.levelup

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levelup_addresses")
data class LevelUpAddressEntity(
    @PrimaryKey val id: String,
    val run: String,
    val alias: String,
    val street: String,
    val numero: String?,
    val comuna: String,
    val region: String,
    val complement: String?,
    val isPrimary: Boolean
)
