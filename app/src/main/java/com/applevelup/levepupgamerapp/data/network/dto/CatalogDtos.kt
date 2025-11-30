package com.applevelup.levepupgamerapp.data.network.dto

data class ProductDto(
    val codigo: String,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val stock: Int,
    val categoria: String,
    val imagenUrl: String?
)

data class CategoryDto(
    val id: String,
    val nombre: String,
    val descripcion: String?
)

data class RegionDto(
    val codigo: String,
    val nombre: String
)
