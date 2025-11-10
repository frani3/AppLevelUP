package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.domain.repository.PaymentRepository

class PaymentRepositoryImpl : PaymentRepository {

    override fun getPaymentMethods(): List<PaymentMethod> = synchronized(methods) { methods.toList() }

    override fun deletePaymentMethod(id: Int) {
        synchronized(methods) {
            val removedDefault = methods.firstOrNull { it.id == id }?.isDefault == true
            methods.removeAll { it.id == id }
            if (removedDefault && methods.isNotEmpty()) {
                setDefaultLocked(methods.first().id)
            }
        }
    }

    override fun addPaymentMethod(method: PaymentMethod) {
        synchronized(methods) {
            methods.add(0, method)
        }
    }

    override fun setDefaultPaymentMethod(id: Int) {
        synchronized(methods) {
            setDefaultLocked(id)
        }
    }

    fun reset() {
        synchronized(methods) { methods.clear() }
    }

    private fun setDefaultLocked(id: Int) {
        if (methods.none { it.id == id }) return
        val updated = methods.map { it.copy(isDefault = it.id == id) }
        methods.clear()
        methods.addAll(updated)
    }

    companion object {
        private val methods = mutableListOf<PaymentMethod>()
    }
}
