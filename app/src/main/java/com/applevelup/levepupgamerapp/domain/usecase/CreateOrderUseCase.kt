package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.domain.repository.CartRepository
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import com.applevelup.levepupgamerapp.domain.repository.UserRepository
import com.applevelup.levepupgamerapp.utils.PriceUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class CreateOrderUseCase(
    private val cartRepository: CartRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        items: List<CartItem>,
        shippingCost: Double,
        address: Address,
        paymentMethod: PaymentMethod
    ): Order {
        if (items.isEmpty()) {
            throw IllegalStateException("No hay productos en el carrito")
        }

        val session = sessionRepository.getSession()
        val userId = session.userId ?: throw IllegalStateException("Sesión inválida")

        val totalAmount = items.sumOf { it.price * it.quantity } + shippingCost
        val order = Order(
            id = generateOrderId(address, paymentMethod),
            date = currentDate(),
            status = "Pagado",
            total = PriceUtils.formatPriceCLP(totalAmount),
            itemCount = items.sumOf { it.quantity }
        )

        userRepository.addOrder(order)
        cartRepository.clearCart(userId)

        return order
    }

    private fun generateOrderId(address: Address, paymentMethod: PaymentMethod): String {
        val addressTag = address.alias
            .filter { it.isLetter() }
            .take(2)
            .uppercase(Locale.getDefault())
            .ifBlank { "XX" }
        val paymentTag = paymentMethod.lastFourDigits.takeLast(2).ifBlank { "00" }
        val suffix = Random.nextInt(1000, 9999)
        return "ORD-$addressTag$paymentTag-$suffix"
    }

    private fun currentDate(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return formatter.format(Date())
    }
}
