package com.applevelup.levepupgamerapp.data.network.dto

data class ProductDto(
    val codigo: String,
    val nombre: String,
    val descripcion: String?,
    val categoria: String,
    val fabricante: String?,
    val distribuidor: String?,
    val precio: Double,
    val stock: Int,
    val stockCritico: Int?,
    val imagenUrl: String?,
    val eliminado: Boolean?,
    val deletedAt: String?,
    val createdAt: String?,
    val updatedAt: String?
)

data class CreateProductRequestDto(
    val codigo: String?,
    val nombre: String,
    val descripcion: String?,
    val categoria: String,
    val fabricante: String?,
    val distribuidor: String?,
    val precio: Double,
    val stock: Int?,
    val stockCritico: Int?,
    val imagenUrl: String?
)

data class CategoryDto(
    val id: Int,
    val nombre: String,
    val eliminada: Boolean?,
    val createdAt: String?,
    val updatedAt: String?
)

data class RegionDto(
    val id: Int,
    val nombre: String,
    val comunas: List<String>
)
