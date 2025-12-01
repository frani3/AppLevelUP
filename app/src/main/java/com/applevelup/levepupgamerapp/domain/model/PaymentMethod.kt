package com.applevelup.levepupgamerapp.domain.model

enum class CardType { VISA, MASTERCARD, DEBIT, CASH, TRANSFER, OTHER }

data class PaymentMethod(
    val id: Int,
    val cardType: CardType,
    val cardBrand: String = "",
    val lastFourDigits: String = "",
    val last4: String = lastFourDigits,
    val expiryDate: String = "",
    val isDefault: Boolean = false
)
