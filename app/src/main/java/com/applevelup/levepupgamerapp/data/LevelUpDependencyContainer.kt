package com.applevelup.levepupgamerapp.data

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.mapper.LevelUpAddressMapper
import com.applevelup.levepupgamerapp.data.mapper.LevelUpCategoryMapper
import com.applevelup.levepupgamerapp.data.mapper.LevelUpProductMapper
import com.applevelup.levepupgamerapp.data.mapper.LevelUpRegionMapper
import com.applevelup.levepupgamerapp.data.mapper.LevelUpStatsMapper
import com.applevelup.levepupgamerapp.data.mapper.LevelUpUserMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpNetworkModule
import com.applevelup.levepupgamerapp.data.network.session.SessionTokenProvider
import com.applevelup.levepupgamerapp.data.repository.mobile.LevelUpAddressRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.mobile.LevelUpAuthRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.mobile.LevelUpCategoryRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.mobile.LevelUpProductRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.mobile.LevelUpRegionRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.mobile.LevelUpStatsRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.mobile.LevelUpUserRepositoryImpl
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAddressRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpCategoryRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpProductRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpRegionRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpStatsRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpUserRepository

object LevelUpDependencyContainer {

    private val application get() = LevelUpApplication.instance

    private val cacheDao by lazy { application.appDatabase.cacheMetadataDao() }
    private val statsMapper by lazy { LevelUpStatsMapper() }
    private val userMapper by lazy { LevelUpUserMapper(statsMapper) }

    private val productMapper by lazy { LevelUpProductMapper() }
    private val categoryMapper by lazy { LevelUpCategoryMapper() }
    private val regionMapper by lazy { LevelUpRegionMapper() }
    private val addressMapper by lazy { LevelUpAddressMapper() }

    private val tokenProvider by lazy {
        SessionTokenProvider(application.sessionPreferencesDataSource)
    }

    private val mobileApi by lazy {
        LevelUpNetworkModule.createApi(tokenProvider)
    }

    val authRepository: LevelUpAuthRepository by lazy {
        LevelUpAuthRepositoryImpl(mobileApi, tokenProvider, userRepository, userMapper)
    }

    val userRepository: LevelUpUserRepository by lazy {
        LevelUpUserRepositoryImpl(mobileApi, userMapper)
    }

    val productRepository: LevelUpProductRepository by lazy {
        LevelUpProductRepositoryImpl(
            api = mobileApi,
            productDao = application.appDatabase.levelUpProductDao(),
            cacheMetadataDao = cacheDao,
            mapper = productMapper
        )
    }

    val categoryRepository: LevelUpCategoryRepository by lazy {
        LevelUpCategoryRepositoryImpl(
            api = mobileApi,
            categoryDao = application.appDatabase.levelUpCategoryDao(),
            cacheMetadataDao = cacheDao,
            mapper = categoryMapper
        )
    }

    val regionRepository: LevelUpRegionRepository by lazy {
        LevelUpRegionRepositoryImpl(
            api = mobileApi,
            regionDao = application.appDatabase.levelUpRegionDao(),
            cacheMetadataDao = cacheDao,
            mapper = regionMapper
        )
    }

    val addressRepository: LevelUpAddressRepository by lazy {
        LevelUpAddressRepositoryImpl(
            api = mobileApi,
            addressDao = application.appDatabase.levelUpAddressDao(),
            cacheMetadataDao = cacheDao,
            mapper = addressMapper
        )
    }

    val statsRepository: LevelUpStatsRepository by lazy {
        LevelUpStatsRepositoryImpl(mobileApi, statsMapper)
    }
}
