package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {

    private var user = UserProfile(
        name = "FranI3",
        email = "frani3@email.com",
        avatarRes = R.drawable.avatar_placeholder,
        orderCount = 12,
        wishlistCount = 8,
        couponCount = 3
    )

    private val orders = mutableListOf(
        Order("#345-1", "15 Oct 2025", "Entregado", "$149.990", 2),
        Order("#312-8", "02 Oct 2025", "Entregado", "$89.990", 1),
        Order("#299-3", "21 Sep 2025", "Cancelado", "$29.990", 1)
    )

    override fun getUserProfile(): UserProfile = user

    override fun getUserOrders(): List<Order> = orders

    override fun logout() {
        // TODO: limpiar sesión cuando uses DataStore o Firebase
    }

    override fun updateUser(fullName: String, email: String, newPassword: String?) {
        // Simula guardar los cambios localmente.
        user = user.copy(
            name = fullName,
            email = email
            // No guardamos la contraseña por seguridad,
            // pero podrías persistirla en DataStore en una implementación real.
        )

        // Si tuvieras persistencia local, aquí guardarías el nuevo estado.
        println("Usuario actualizado: $fullName, $email (password: ${newPassword != null})")
    }
}
