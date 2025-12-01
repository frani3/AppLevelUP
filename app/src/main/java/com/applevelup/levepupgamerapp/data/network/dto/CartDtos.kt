package com.applevelup.levepupgamerapp.data.network.dto

/**
 * Response DTO for GET /carts/me and PUT /carts/me
 */
data class CartDto(
    val userRun: String,
    val items: List<CartItemDto>,
    val totalQuantity: Int,
    val updatedAt: String?
)

/**
 * Item in cart response
 */
data class CartItemDto(
    val productCode: String,
    val quantity: Int
)

/**
 * Request DTO for PUT /carts/me
 */
data class UpdateCartRequestDto(
    val items: List<CartItemRequestDto>,
    val forceReplace: Boolean = false
)

/**
 * Item in cart request
 */
data class CartItemRequestDto(
    val productCode: String,
    val quantity: Int
)
