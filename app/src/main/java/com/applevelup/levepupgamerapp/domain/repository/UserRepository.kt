package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile

interface UserRepository {
    fun getUserProfile(): UserProfile
    fun getUserOrders(): List<Order>
    fun logout()
}
