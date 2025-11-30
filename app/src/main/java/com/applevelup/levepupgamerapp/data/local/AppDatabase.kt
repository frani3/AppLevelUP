package com.applevelup.levepupgamerapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.applevelup.levepupgamerapp.data.local.dao.CacheMetadataDao
import com.applevelup.levepupgamerapp.data.local.dao.CartDao
import com.applevelup.levepupgamerapp.data.local.dao.LevelUpAddressDao
import com.applevelup.levepupgamerapp.data.local.dao.LevelUpCategoryDao
import com.applevelup.levepupgamerapp.data.local.dao.LevelUpProductDao
import com.applevelup.levepupgamerapp.data.local.dao.LevelUpRegionDao
import com.applevelup.levepupgamerapp.data.local.dao.PaymentMethodDao
import com.applevelup.levepupgamerapp.data.local.dao.ProductDao
import com.applevelup.levepupgamerapp.data.local.dao.UserDao
import com.applevelup.levepupgamerapp.data.local.cache.CacheMetadataEntity
import com.applevelup.levepupgamerapp.data.local.entity.CartItemEntity
import com.applevelup.levepupgamerapp.data.local.entity.PaymentMethodEntity
import com.applevelup.levepupgamerapp.data.local.entity.ProductEntity
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity
import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpAddressEntity
import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpCategoryEntity
import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpProductEntity
import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpRegionEntity
import com.applevelup.levepupgamerapp.data.local.seed.LocalSeedData

@Database(
	entities = [
		ProductEntity::class,
		CartItemEntity::class,
		UserEntity::class,
		PaymentMethodEntity::class,
		LevelUpProductEntity::class,
		LevelUpCategoryEntity::class,
		LevelUpRegionEntity::class,
		LevelUpAddressEntity::class,
		CacheMetadataEntity::class
	],
	version = 7,
	exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

	abstract fun productDao(): ProductDao
	abstract fun cartDao(): CartDao
	abstract fun userDao(): UserDao
	abstract fun paymentMethodDao(): PaymentMethodDao
	abstract fun levelUpProductDao(): LevelUpProductDao
	abstract fun levelUpCategoryDao(): LevelUpCategoryDao
	abstract fun levelUpRegionDao(): LevelUpRegionDao
	abstract fun levelUpAddressDao(): LevelUpAddressDao
	abstract fun cacheMetadataDao(): CacheMetadataDao

	suspend fun seed() {
		val productDao = productDao()
		productDao.upsertProducts(LocalSeedData.defaultProducts)

		val userDao = userDao()
		LocalSeedData.seededUsers.forEach { seed ->
			val existing = userDao.findByEmail(seed.email)
			if (existing == null) {
				userDao.insertUser(seed.copy(id = 0))
			}
		}

		if (userDao.countSuperAdmins() == 0) {
			userDao.insertUser(LocalSeedData.superAdmin.copy(id = 0))
		}
	}

	companion object {
		private const val DB_NAME = "levelup_gamer_app.db"

		@Volatile
		private var instance: AppDatabase? = null

		fun build(context: Context): AppDatabase {
			return instance ?: synchronized(this) {
				instance ?: Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					DB_NAME
				).fallbackToDestructiveMigration()
					.build()
					.also { instance = it }
			}
		}
	}
}