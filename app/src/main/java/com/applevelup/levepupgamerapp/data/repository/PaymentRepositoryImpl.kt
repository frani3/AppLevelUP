package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.domain.model.CardType
import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.domain.repository.PaymentRepository

class PaymentRepositoryImpl : PaymentRepository {

    override fun getPaymentMethods(): List<PaymentMethod> = methods.toList()

    override fun deletePaymentMethod(id: Int) {
        val removedDefault = methods.firstOrNull { it.id == id }?.isDefault == true
        methods.removeAll { it.id == id }
        if (removedDefault && methods.isNotEmpty()) {
            setDefaultPaymentMethod(methods.first().id)
        }
    }

    override fun addPaymentMethod(method: PaymentMethod) {
        methods.add(0, method)
    }

    override fun setDefaultPaymentMethod(id: Int) {
        if (methods.none { it.id == id }) return
        val updated = methods.map { it.copy(isDefault = it.id == id) }
        methods.clear()
        methods.addAll(updated)
    }

    companion object {
        private val methods = mutableListOf(
            PaymentMethod(1, CardType.VISA, "4242", "12/27", true),
            PaymentMethod(2, CardType.MASTERCARD, "5578", "08/26", false)
        )
    }
}
