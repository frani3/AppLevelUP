package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.domain.repository.PaymentRepository

class GetPaymentMethodsUseCase(private val repo: PaymentRepository) {
    suspend operator fun invoke(): List<PaymentMethod> = repo.getPaymentMethods()
}
