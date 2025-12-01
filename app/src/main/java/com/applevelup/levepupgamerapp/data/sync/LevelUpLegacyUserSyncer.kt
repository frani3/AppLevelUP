package com.applevelup.levepupgamerapp.data.sync

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.local.dao.UserDao
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.sync.LegacyUserSyncer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LevelUpLegacyUserSyncer : LegacyUserSyncer {
    private val userDao: UserDao = LevelUpApplication.database.userDao()

    override suspend fun replaceWith(profile: LevelUpUserProfile) {
        withContext(Dispatchers.IO) {
            val existing = userDao.getPrimaryUser()
            val (firstName, lastName) = splitName(profile.name)
            val stats = profile.stats
            val experience = stats?.experience

            val entity = UserEntity(
                id = existing?.id ?: 0L,
                run = profile.run,
                fullName = profile.name.ifBlank { profile.email },
                firstName = firstName,
                lastName = lastName,
                email = profile.email,
                avatarRes = R.drawable.avatar_placeholder,
                orderCount = experience?.compras ?: 0,
                wishlistCount = experience?.torneos ?: 0,
                couponCount = experience?.referidos ?: 0,
                profileRole = "Cliente",
                birthDate = null,
                region = profile.region,
                comuna = profile.commune,
                address = profile.address,
                referralCode = stats?.referralCode,
                hasLifetimeDiscount = !stats?.referralCode.isNullOrBlank(),
                isSuperAdmin = false,
                isSystem = false,
                photoUri = null
            )

            userDao.insertUser(entity)
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            userDao.getPrimaryUser()?.let { userDao.deleteUser(it.id) }
        }
    }

    private fun splitName(fullName: String): Pair<String?, String?> {
        val cleaned = fullName.trim()
        if (cleaned.isEmpty()) return null to null
        val parts = cleaned.split(" ", limit = 2)
        val first = parts.getOrNull(0)?.takeIf { it.isNotBlank() }
        val last = parts.getOrNull(1)?.takeIf { it.isNotBlank() }
        return first to last
    }
}
