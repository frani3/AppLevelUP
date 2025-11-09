package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.User
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUserProfile(): Flow<UserProfile?>
    suspend fun getUserProfile(): UserProfile?
    suspend fun getUserOrders(): List<Order>
    suspend fun authenticate(email: String, password: String): User?
    suspend fun logout()
    suspend fun updateUser(fullName: String, email: String, newPassword: String?)
}
