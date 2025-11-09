package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.local.dao.UserDao
import com.applevelup.levepupgamerapp.data.mapper.UserMapper
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.repository.UserRepository
import com.applevelup.levepupgamerapp.utils.SecurityUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userDao: UserDao = LevelUpApplication.database.userDao()
) : UserRepository {

    private val orders = listOf(
        Order("#345-1", "15 Oct 2025", "Entregado", "$149.990", 2),
        Order("#312-8", "02 Oct 2025", "Entregado", "$89.990", 1),
        Order("#299-3", "21 Sep 2025", "Cancelado", "$29.990", 1)
    )

    override fun observeUserProfile(): Flow<UserProfile?> {
        return userDao.observePrimaryUser().map { entity ->
            val profileEntity = entity ?: userDao.getSuperAdmin()
            profileEntity?.let(UserMapper::toProfile)
        }
    }

    override suspend fun getUserProfile(): UserProfile? {
        val entity = userDao.getPrimaryUser() ?: userDao.getSuperAdmin()
        return entity?.let(UserMapper::toProfile)
    }

    override suspend fun getUserOrders(): List<Order> = orders

    override suspend fun logout() {
        // TODO: limpiar sesión cuando se agregue DataStore
    }

    override suspend fun updateUser(fullName: String, email: String, newPassword: String?) {
        val current = userDao.getPrimaryUser() ?: return
        if (current.isSuperAdmin) {
            return
        }

        userDao.updateUser(current.id, fullName, email)
        if (!newPassword.isNullOrBlank()) {
            val hashed = SecurityUtils.hashPassword(newPassword)
            userDao.updatePassword(current.id, hashed)
        }
    }
}
