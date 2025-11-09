package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.PaymentMethod

interface PaymentRepository {
    fun getPaymentMethods(): List<PaymentMethod>
    fun deletePaymentMethod(id: Int)
    fun addPaymentMethod(method: PaymentMethod)
    fun setDefaultPaymentMethod(id: Int)

}
