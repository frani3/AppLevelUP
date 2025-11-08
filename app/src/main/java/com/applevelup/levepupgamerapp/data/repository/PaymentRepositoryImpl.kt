package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.domain.model.CardType
import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.domain.repository.PaymentRepository

class PaymentRepositoryImpl : PaymentRepository {

    private val methods = mutableListOf(
        PaymentMethod(1, CardType.VISA, "4242", "12/27", true),
        PaymentMethod(2, CardType.MASTERCARD, "5578", "08/26", false)
    )

    override fun getPaymentMethods(): List<PaymentMethod> = methods.toList()

    override fun deletePaymentMethod(id: Int) {
        methods.removeAll { it.id == id }
    }
}
