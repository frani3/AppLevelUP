package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.CardType
import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.domain.repository.PaymentRepository

class AddPaymentMethodUseCase(private val repo: PaymentRepository) {
    operator fun invoke(
        name: String,
        number: String,
        expiry: String
    ) {
        val cardType = when {
            number.startsWith("4") -> CardType.VISA
            number.startsWith("5") -> CardType.MASTERCARD
            else -> CardType.OTHER
        }

        val lastFour = number.takeLast(4)
        val method = PaymentMethod(
            id = (100..999).random(),
            cardType = cardType,
            lastFourDigits = lastFour,
            expiryDate = expiry,
            isDefault = false
        )
        repo.addPaymentMethod(method)
    }
}
