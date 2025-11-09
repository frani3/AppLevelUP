package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(private val repo: UserRepository) {
    fun observeProfile(): Flow<UserProfile?> = repo.observeUserProfile()
    suspend fun getUserProfile(): UserProfile? = repo.getUserProfile()
    suspend fun getOrders(): List<Order> = repo.getUserOrders()
    suspend fun logout() = repo.logout()
}
