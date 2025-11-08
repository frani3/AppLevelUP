package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.repository.UserRepository

class GetUserProfileUseCase(private val repo: UserRepository) {
    fun getUserProfile(): UserProfile = repo.getUserProfile()
    fun getOrders(): List<Order> = repo.getUserOrders()
    fun logout() = repo.logout()
}
