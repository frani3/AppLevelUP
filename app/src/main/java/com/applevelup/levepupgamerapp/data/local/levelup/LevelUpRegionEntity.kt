package com.applevelup.levepupgamerapp.data.local.levelup

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levelup_regions")
data class LevelUpRegionEntity(
    @PrimaryKey val code: String,
    val name: String
)
