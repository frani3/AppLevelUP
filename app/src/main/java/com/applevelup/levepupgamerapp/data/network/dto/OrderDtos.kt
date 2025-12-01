package com.applevelup.levepupgamerapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class OrderDto(
    val id: String,
    val items: List<OrderItemDto>,
    val direccion: String,
    val region: String,
    val comuna: String,
    val paymentMethod: String,
    val status: String,
    val subtotal: Double,
    val shippingCost: Double,
    val total: Double,
    val createdAt: String,
    val updatedAt: String
)

data class OrderItemDto(
    val codigo: String,
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val imageUrl: String?
)

data class CreateOrderRequestDto(
    val items: List<OrderItemRequestDto>,
    val direccion: String,
    val region: String,
    val comuna: String,
    val paymentMethod: String
)

data class OrderItemRequestDto(
    val codigo: String,
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Double
)
