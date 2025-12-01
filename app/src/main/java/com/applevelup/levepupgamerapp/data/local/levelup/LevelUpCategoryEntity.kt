package com.applevelup.levepupgamerapp.data.local.levelup

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levelup_categories")
data class LevelUpCategoryEntity(
    @PrimaryKey val id: Int,
    val name: String
)
