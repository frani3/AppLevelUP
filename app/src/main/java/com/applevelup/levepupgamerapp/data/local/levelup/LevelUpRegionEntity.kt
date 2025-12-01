package com.applevelup.levepupgamerapp.data.local.levelup

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ComunasConverter {
    @TypeConverter
    fun fromList(list: List<String>): String = Gson().toJson(list)
    
    @TypeConverter
    fun toList(json: String): List<String> = 
        Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
}

@Entity(tableName = "levelup_regions")
@TypeConverters(ComunasConverter::class)
data class LevelUpRegionEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val comunas: List<String>
)
