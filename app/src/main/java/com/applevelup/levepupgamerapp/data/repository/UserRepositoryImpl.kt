package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    override fun getUserProfile() = UserProfile(
        name = "FranI3",
        email = "frani3@email.com",
        avatarRes = R.drawable.avatar_placeholder,
        orderCount = 12,
        wishlistCount = 8,
        couponCount = 3
    )

    override fun getUserOrders() = listOf(
        Order("#345-1", "15 Oct 2025", "Entregado", "$149.990", 2),
        Order("#312-8", "02 Oct 2025", "Entregado", "$89.990", 1),
        Order("#299-3", "21 Sep 2025", "Cancelado", "$29.990", 1)
    )

    override fun logout() {
        // TODO: limpiar sesión con DataStore o Firebase
    }
}
