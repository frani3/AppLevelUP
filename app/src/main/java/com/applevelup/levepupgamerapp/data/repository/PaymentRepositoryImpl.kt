package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.local.dao.PaymentMethodDao
import com.applevelup.levepupgamerapp.data.local.entity.PaymentMethodEntity
import com.applevelup.levepupgamerapp.domain.model.CardType
import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.domain.repository.PaymentRepository

class PaymentRepositoryImpl(
    private val dao: PaymentMethodDao = LevelUpApplication.database.paymentMethodDao()
) : PaymentRepository {

    override suspend fun getPaymentMethods(): List<PaymentMethod> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun deletePaymentMethod(id: Int) {
        val existing = dao.findById(id.toLong()) ?: return
        dao.deleteById(id.toLong())
        if (existing.isDefault) {
            val remaining = dao.getAll()
            if (remaining.isNotEmpty()) {
                dao.setDefault(remaining.first().id)
            }
        }
    }

    override suspend fun addPaymentMethod(method: PaymentMethod): PaymentMethod {
        val entity = method.toEntity()
        val newId = dao.insert(entity)
        val stored = dao.findById(newId) ?: return method.copy(id = newId.toInt())
        return stored.toDomain()
    }

    override suspend fun setDefaultPaymentMethod(id: Int) {
        val existing = dao.findById(id.toLong()) ?: return
        dao.setDefault(existing.id)
    }

    override suspend fun clearPaymentMethods() {
        dao.clearAll()
    }

    private fun PaymentMethodEntity.toDomain(): PaymentMethod {
        val type = when (cardType) {
            CardType.VISA.name -> CardType.VISA
            CardType.MASTERCARD.name -> CardType.MASTERCARD
            else -> CardType.OTHER
        }
        return PaymentMethod(
            id = id.toInt(),
            cardType = type,
            lastFourDigits = lastFourDigits,
            expiryDate = expiryDate,
            isDefault = isDefault
        )
    }

    private fun PaymentMethod.toEntity() = PaymentMethodEntity(
        id = if (id == 0) 0 else id.toLong(),
        cardType = cardType.name,
        lastFourDigits = lastFourDigits,
        expiryDate = expiryDate,
        isDefault = isDefault,
        createdAt = System.currentTimeMillis()
    )
}
