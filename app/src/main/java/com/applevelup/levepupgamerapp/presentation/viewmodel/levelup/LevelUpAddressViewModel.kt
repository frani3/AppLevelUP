package com.applevelup.levepupgamerapp.presentation.viewmodel.levelup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.usecase.levelup.CreateAddressUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.DeleteAddressUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.ListAddressesUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.SetPrimaryAddressUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.UpdateAddressUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LevelUpAddressViewModel(
    private val listAddressesUseCase: ListAddressesUseCase,
    private val createAddressUseCase: CreateAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val setPrimaryAddressUseCase: SetPrimaryAddressUseCase
) : ViewModel() {

    fun observeAddresses(run: String): kotlinx.coroutines.flow.StateFlow<LevelUpResource<List<LevelUpAddress>>> {
        return listAddressesUseCase(run)
            .stateIn(viewModelScope, SharingStarted.Lazily, LevelUpResource.Loading)
    }

    fun refreshAddresses(run: String, forceRefresh: Boolean = true) {
        viewModelScope.launch {
            listAddressesUseCase(run, forceRefresh).first()
        }
    }

    suspend fun createAddress(run: String, input: AddressInput): LevelUpResult<LevelUpAddress> {
        return createAddressUseCase(run, input)
    }

    suspend fun updateAddress(run: String, addressId: String, input: AddressInput): LevelUpResult<LevelUpAddress> {
        return updateAddressUseCase(run, addressId, input)
    }

    suspend fun deleteAddress(run: String, addressId: String): LevelUpResult<Unit> {
        return deleteAddressUseCase(run, addressId)
    }

    suspend fun setPrimaryAddress(run: String, addressId: String): LevelUpResult<Unit> {
        return setPrimaryAddressUseCase(run, addressId)
    }
}
