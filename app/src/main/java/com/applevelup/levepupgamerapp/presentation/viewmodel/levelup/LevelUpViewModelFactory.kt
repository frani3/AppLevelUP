package com.applevelup.levepupgamerapp.presentation.viewmodel.levelup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.usecase.levelup.CreateAddressUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.DeleteAddressUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchCategoriesUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchLevelUpStatsUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchProductsUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchRegionsUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchUserProfileUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.ListAddressesUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.LoginUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.LogoutUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.RegisterUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.SetPrimaryAddressUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.UpdateAddressUseCase

class LevelUpViewModelFactory : ViewModelProvider.Factory {

    private val dependencies = LevelUpDependencyContainer

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepository = dependencies.authRepository
        return when (modelClass) {
            LevelUpAuthViewModel::class.java -> LevelUpAuthViewModel(
                loginUseCase = LoginUseCase(authRepository),
                registerUseCase = RegisterUseCase(authRepository),
                fetchProfileUseCase = FetchUserProfileUseCase(dependencies.userRepository),
                logoutUseCase = LogoutUseCase(authRepository)
            )
            LevelUpCatalogViewModel::class.java -> LevelUpCatalogViewModel(
                fetchProductsUseCase = FetchProductsUseCase(dependencies.productRepository),
                fetchCategoriesUseCase = FetchCategoriesUseCase(dependencies.categoryRepository)
            )
            LevelUpAddressViewModel::class.java -> LevelUpAddressViewModel(
                listAddressesUseCase = ListAddressesUseCase(dependencies.addressRepository),
                createAddressUseCase = CreateAddressUseCase(dependencies.addressRepository),
                updateAddressUseCase = UpdateAddressUseCase(dependencies.addressRepository),
                deleteAddressUseCase = DeleteAddressUseCase(dependencies.addressRepository),
                setPrimaryAddressUseCase = SetPrimaryAddressUseCase(dependencies.addressRepository)
            )
            LevelUpStatsViewModel::class.java -> LevelUpStatsViewModel(
                FetchLevelUpStatsUseCase(dependencies.statsRepository)
            )
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        } as T
    }
}
