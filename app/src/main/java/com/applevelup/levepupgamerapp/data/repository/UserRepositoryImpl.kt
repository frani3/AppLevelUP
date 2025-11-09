package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.local.dao.UserDao
import com.applevelup.levepupgamerapp.data.mapper.UserMapper
import com.applevelup.levepupgamerapp.data.prefs.SessionPreferencesDataSource
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.User
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.repository.UserRepository
import com.applevelup.levepupgamerapp.utils.SecurityUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userDao: UserDao = LevelUpApplication.database.userDao(),
    private val sessionPrefs: SessionPreferencesDataSource = LevelUpApplication.sessionPreferences
) : UserRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeUserProfile(): Flow<UserProfile?> {
        return sessionPrefs.sessionFlow.flatMapLatest { session ->
            if (!session.isLoggedIn) {
                flowOf(null)
            } else {
                val targetId = session.userId
                if (targetId != null) {
                    userDao.observeUserById(targetId).map { entity ->
                        val resolved = entity ?: userDao.getSuperAdmin()
                        resolved?.let(UserMapper::toProfile)
                    }
                } else {
                    flow {
                        emit(userDao.getSuperAdmin()?.let(UserMapper::toProfile))
                    }
                }
            }
        }
    }

    override suspend fun getUserProfile(): UserProfile? {
        val session = sessionPrefs.sessionFlow.first()
        if (!session.isLoggedIn) return null

        val entity = session.userId?.let { userDao.getUserById(it) } ?: userDao.getSuperAdmin()
        return entity?.let(UserMapper::toProfile)
    }

    override suspend fun getUserOrders(): List<Order> = synchronized(orders) { orders.toList() }

    override suspend fun addOrder(order: Order) {
        synchronized(orders) {
            orders.add(0, order)
        }
        incrementUserOrderCount()
    }

    override suspend fun authenticate(email: String, password: String): User? {
        val normalizedEmail = email.trim()
        val entity = userDao.findByEmail(normalizedEmail) ?: return null
        val hashed = SecurityUtils.hashPassword(password)
        if (entity.passwordHash != hashed) {
            return null
        }

        return UserMapper.toUser(entity)
    }

    override suspend fun logout() {
        sessionPrefs.clearSession()
    }

    override suspend fun updateUser(fullName: String, email: String, newPassword: String?) {
        val session = sessionPrefs.sessionFlow.first()
        val userId = session.userId ?: return

        val current = userDao.getUserById(userId) ?: return
        if (current.isSuperAdmin) return

        userDao.updateUser(current.id, fullName, email)
        if (!newPassword.isNullOrBlank()) {
            val hashed = SecurityUtils.hashPassword(newPassword)
            userDao.updatePassword(current.id, hashed)
        }

        sessionPrefs.updateSession { state ->
            val shouldPersistEmail = state.rememberMe
            state.copy(
                fullName = fullName,
                email = if (shouldPersistEmail) email else null
            )
        }
    }

    override suspend fun updateProfilePhoto(photoUri: String) {
        val session = sessionPrefs.sessionFlow.first()
        val targetUser = when (val userId = session.userId) {
            null -> userDao.getPrimaryUser() ?: userDao.getSuperAdmin()
            else -> userDao.getUserById(userId)
        } ?: return

        if (targetUser.isSuperAdmin) {
            userDao.insertUser(targetUser.copy(photoUri = photoUri))
        } else {
            userDao.updatePhoto(targetUser.id, photoUri)
        }
    }
    private suspend fun incrementUserOrderCount() {
        val session = sessionPrefs.sessionFlow.first()
        val userId = session.userId ?: return
        val entity = userDao.getUserById(userId) ?: return
        val updated = entity.copy(orderCount = entity.orderCount + 1)
        userDao.insertUser(updated)
    }

    companion object {
        private val orders = mutableListOf(
            Order("#345-1", "15 Oct 2025", "Entregado", "$149.990", 2),
            Order("#312-8", "02 Oct 2025", "Entregado", "$89.990", 1),
            Order("#299-3", "21 Sep 2025", "Cancelado", "$29.990", 1)
        )
    }
}
