package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAddressRepository

class SetPrimaryAddressUseCase(private val repository: LevelUpAddressRepository) {

    suspend operator fun invoke(run: String, addressId: String): LevelUpResult<Unit> {
        return repository.setPrimaryAddress(run, addressId)
    }
}
