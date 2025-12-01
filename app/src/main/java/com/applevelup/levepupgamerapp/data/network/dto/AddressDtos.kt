package com.applevelup.levepupgamerapp.data.network.dto

/**
 * Response DTO for GET /users/{run}/addresses
 */
data class AddressDto(
    val id: String,
    val fullName: String,
    val line1: String,
    val city: String,
    val region: String,
    val country: String,
    val isPrimary: Boolean,
    val createdAt: String?,
    val updatedAt: String?
)

/**
 * Request DTO for POST/PUT /users/{run}/addresses
 */
data class AddressRequestDto(
    val fullName: String,
    val line1: String,
    val city: String,
    val region: String,
    val country: String = "Chile",
    val isPrimary: Boolean = false
)
