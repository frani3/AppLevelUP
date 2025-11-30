package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAddressRepository

class UpdateAddressUseCase(private val repository: LevelUpAddressRepository) {

    suspend operator fun invoke(run: String, addressId: String, input: AddressInput): LevelUpResult<LevelUpAddress> {
        return repository.updateAddress(run, addressId, input)
    }
}
