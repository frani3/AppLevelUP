package com.applevelup.levepupgamerapp.domain.model.levelup

import kotlin.math.absoluteValue

data class LevelUpProduct(
    val code: String,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: String,
    val description: String?,
    val imageUrl: String?
) {
    // ID generado a partir del código para compatibilidad con navegación existente
    val id: Int get() = code.hashCode().absoluteValue
}
