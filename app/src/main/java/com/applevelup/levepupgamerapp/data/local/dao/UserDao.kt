package com.applevelup.levepupgamerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

	@Query("SELECT * FROM users WHERE is_super_admin = 0 ORDER BY id LIMIT 1")
	fun observePrimaryUser(): Flow<UserEntity?>

	@Query("SELECT * FROM users WHERE is_super_admin = 0 ORDER BY id LIMIT 1")
	suspend fun getPrimaryUser(): UserEntity?

	@Query("SELECT * FROM users WHERE id = :id")
	suspend fun getUserById(id: Long): UserEntity?

	@Query("SELECT * FROM users WHERE id = :id")
	fun observeUserById(id: Long): Flow<UserEntity?>

	@Query("SELECT * FROM users WHERE email = :email LIMIT 1")
	suspend fun findByEmail(email: String): UserEntity?

	@Query("SELECT * FROM users WHERE is_super_admin = 1 LIMIT 1")
	suspend fun getSuperAdmin(): UserEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertUser(user: UserEntity): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertUsers(users: List<UserEntity>)

	@Query("UPDATE users SET full_name = :fullName, email = :email WHERE id = :id AND is_super_admin = 0")
	suspend fun updateUser(id: Long, fullName: String, email: String)

	@Query("UPDATE users SET password_hash = :passwordHash WHERE id = :id AND is_super_admin = 0")
	suspend fun updatePassword(id: Long, passwordHash: String)

	@Query("UPDATE users SET photo_uri = :photoUri WHERE id = :id AND is_super_admin = 0")
	suspend fun updatePhoto(id: Long, photoUri: String)

	@Query("DELETE FROM users WHERE id = :id AND is_super_admin = 0")
	suspend fun deleteUser(id: Long)

	@Query("SELECT COUNT(*) FROM users WHERE is_super_admin = 1")
	suspend fun countSuperAdmins(): Int

	@Query("SELECT COUNT(*) FROM users")
	suspend fun countUsers(): Int
}