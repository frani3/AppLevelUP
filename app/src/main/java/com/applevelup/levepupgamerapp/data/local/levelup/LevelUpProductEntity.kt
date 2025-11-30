package com.applevelup.levepupgamerapp.data.local.levelup

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levelup_products")
data class LevelUpProductEntity(
    @PrimaryKey val code: String,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: String,
    val description: String?,
    val imageUrl: String?
)
