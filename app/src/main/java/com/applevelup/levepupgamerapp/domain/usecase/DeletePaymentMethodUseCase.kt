package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.PaymentRepository

class DeletePaymentMethodUseCase(private val repo: PaymentRepository) {
    operator fun invoke(id: Int) = repo.deletePaymentMethod(id)
}
