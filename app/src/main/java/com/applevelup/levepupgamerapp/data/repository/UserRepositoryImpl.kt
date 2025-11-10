package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.local.dao.UserDao
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity
import com.applevelup.levepupgamerapp.data.mapper.UserMapper
import com.applevelup.levepupgamerapp.data.prefs.SessionPreferencesDataSource
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.User
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.domain.exceptions.EmailAlreadyInUseException
import com.applevelup.levepupgamerapp.domain.repository.UserRepository
import com.applevelup.levepupgamerapp.utils.SecurityUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    override suspend fun getUserOrders(): List<Order> {
        val session = sessionPrefs.sessionFlow.first()
        val userId = session.userId ?: return emptyList()
        return synchronized(orderBook) {
            orderBook[userId]?.toList() ?: emptyList()
        }
    }

    override suspend fun addOrder(order: Order) {
        val session = sessionPrefs.sessionFlow.first()
        val userId = session.userId ?: return
        synchronized(orderBook) {
            val userOrders = orderBook.getOrPut(userId) { mutableListOf() }
            userOrders.add(0, order)
        }
        incrementUserOrderCount()
    }

    override suspend fun findUserByEmail(email: String): User? {
        val normalizedEmail = email.trim().lowercase()
        val entity = userDao.findByEmail(normalizedEmail) ?: return null
        return UserMapper.toUser(entity)
    }

    override suspend fun authenticate(email: String, password: String): User? {
        val normalizedEmail = email.trim().lowercase()
        val entity = userDao.findByEmail(normalizedEmail) ?: return null

        if (entity.passwordHash.isNullOrBlank()) {
            return UserMapper.toUser(entity)
        }

        if (password.isBlank()) {
            return null
        }

        val hashed = SecurityUtils.hashPassword(password)
        if (entity.passwordHash != hashed) {
            return null
        }

        return UserMapper.toUser(entity)
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        run: String,
        email: String,
        password: String,
        birthDate: String,
        region: String,
        comuna: String,
        address: String,
        referralCode: String?
    ): User {
        val normalizedEmail = email.trim().lowercase()
        if (normalizedEmail.isBlank()) {
            throw IllegalArgumentException("Email inválido")
        }

        val cleanedFirstName = firstName.trim()
        if (cleanedFirstName.isBlank()) {
            throw IllegalArgumentException("El nombre es obligatorio")
        }

        val cleanedLastName = lastName.trim()
        if (cleanedLastName.isBlank()) {
            throw IllegalArgumentException("Los apellidos son obligatorios")
        }

        val sanitizedRun = sanitizeRun(run)
        if (!isValidRun(sanitizedRun)) {
            throw IllegalArgumentException("RUN inválido")
        }

        val sanitizedBirthDate = birthDate.trim()
        val birthDateCalendar = parseBirthDate(sanitizedBirthDate)
            ?: throw IllegalArgumentException("Formato de fecha inválido. Usa DD/MM/AAAA")

        val computedAge = calculateAge(birthDateCalendar)
        if (computedAge < MIN_AGE || computedAge > MAX_AGE) {
            throw IllegalArgumentException("La edad debe estar entre $MIN_AGE y $MAX_AGE años")
        }

        val sanitizedRegion = region.trim()
        if (sanitizedRegion.isBlank()) {
            throw IllegalArgumentException("La región es obligatoria")
        }

        val sanitizedComuna = comuna.trim()
        if (sanitizedComuna.isBlank()) {
            throw IllegalArgumentException("La comuna es obligatoria")
        }

        val sanitizedAddress = address.trim()
        if (sanitizedAddress.isBlank()) {
            throw IllegalArgumentException("La dirección es obligatoria")
        }

        val existingUser = userDao.findByEmail(normalizedEmail)
        if (existingUser != null) {
            throw EmailAlreadyInUseException(normalizedEmail)
        }
        val hashedPassword = SecurityUtils.hashPassword(password)
        val trimmedFullName = listOf(cleanedFirstName, cleanedLastName)
            .filter { it.isNotBlank() }
            .joinToString(" ")
        val entity = UserEntity(
            fullName = trimmedFullName,
            firstName = cleanedFirstName,
            lastName = cleanedLastName,
            email = normalizedEmail,
            passwordHash = hashedPassword,
            avatarRes = R.drawable.avatar_placeholder,
            profileRole = "Cliente",
            birthDate = sanitizedBirthDate,
            run = sanitizedRun,
            region = sanitizedRegion,
            comuna = sanitizedComuna,
            address = sanitizedAddress,
            referralCode = referralCode?.trim().takeUnless { it.isNullOrBlank() },
            hasLifetimeDiscount = hasDuocLifetimeBenefit(normalizedEmail),
            isSuperAdmin = false,
            isSystem = false
        )

        val newId = userDao.insertUser(entity)
        val persisted = userDao.getUserById(newId) ?: entity.copy(id = newId)

        synchronized(orderBook) {
            orderBook[persisted.id] = mutableListOf()
        }

        return UserMapper.toUser(persisted)
    }

    override suspend fun logout() {
        sessionPrefs.clearSession()
    }

    override suspend fun updateUser(fullName: String, email: String, newPassword: String?) {
        val session = sessionPrefs.sessionFlow.first()
        val userId = session.userId ?: return

        val current = userDao.getUserById(userId) ?: return
        if (current.isSuperAdmin) return

        val trimmedName = fullName.trim()
        val (firstName, lastName) = splitName(trimmedName)
        userDao.updateUser(current.id, trimmedName, firstName, lastName, email.trim().lowercase())
        if (!newPassword.isNullOrBlank()) {
            val hashed = SecurityUtils.hashPassword(newPassword)
            userDao.updatePassword(current.id, hashed)
        }

        val normalizedEmail = email.trim().lowercase()
        sessionPrefs.updateSession { state ->
            val shouldPersistEmail = state.rememberMe
            state.copy(
                fullName = trimmedName,
                email = if (shouldPersistEmail) normalizedEmail else null
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

    private fun splitName(fullName: String): Pair<String?, String?> {
        val cleaned = fullName.trim()
        if (cleaned.isEmpty()) return null to null
        val parts = cleaned.split(" ", limit = 2)
        val first = parts.getOrNull(0)?.takeIf { it.isNotBlank() }
        val last = parts.getOrNull(1)?.takeIf { it.isNotBlank() }
        return first to last
    }

    private fun sanitizeRun(raw: String): String {
        val cleaned = raw.uppercase(Locale.getDefault())
            .replace(".", "")
            .replace(" ", "")
            .replace("-", "")
        if (cleaned.length < 2) return ""
        val numberPart = cleaned.dropLast(1)
        val checkDigit = cleaned.last()
        return "$numberPart-$checkDigit"
    }

    private fun isValidRun(run: String): Boolean {
        if (!run.matches(Regex("^\\d{7,8}-[0-9K]$"))) return false
        val numberPart = run.substringBefore('-')
        val providedDigit = run.substringAfter('-')
        val expectedDigit = calculateRunCheckDigit(numberPart)
        return expectedDigit.equals(providedDigit, ignoreCase = true)
    }

    private fun calculateRunCheckDigit(numberPart: String): String {
        var multiplier = 2
        var sum = 0
        for (digitChar in numberPart.reversed()) {
            val digit = digitChar.digitToIntOrNull() ?: return ""
            sum += digit * multiplier
            multiplier = if (multiplier == 7) 2 else multiplier + 1
        }
        val remainder = 11 - (sum % 11)
        return when (remainder) {
            11 -> "0"
            10 -> "K"
            else -> remainder.toString()
        }
    }

    private fun parseBirthDate(raw: String): Calendar? {
        if (raw.isBlank()) return null
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            isLenient = false
        }
        return try {
            val parsed = formatter.parse(raw) ?: return null
            Calendar.getInstance().apply { time = parsed }
        } catch (_: ParseException) {
            null
        }
    }

    private fun calculateAge(birthDate: Calendar): Int {
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        val currentDayOfYear = today.get(Calendar.DAY_OF_YEAR)
        val birthDayOfYear = birthDate.get(Calendar.DAY_OF_YEAR)
        if (currentDayOfYear < birthDayOfYear) {
            age -= 1
        }
        return age
    }

    private fun hasDuocLifetimeBenefit(email: String): Boolean {
        val domain = email.substringAfter("@", "")
        return domain.contains("duoc")
    }

    companion object {
        private const val MIN_AGE = 18
        private const val MAX_AGE = 120
        private val orderBook = mutableMapOf<Long, MutableList<Order>>()
    }
}
