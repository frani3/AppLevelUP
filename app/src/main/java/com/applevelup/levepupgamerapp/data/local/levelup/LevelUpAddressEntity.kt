package com.applevelup.levepupgamerapp.data.local.levelup

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levelup_addresses")
data class LevelUpAddressEntity(
    @PrimaryKey val id: String,
    val userRun: String,
    val fullName: String,
    val line1: String,
    val city: String,
    val region: String,
    val country: String,
    val isPrimary: Boolean
)
