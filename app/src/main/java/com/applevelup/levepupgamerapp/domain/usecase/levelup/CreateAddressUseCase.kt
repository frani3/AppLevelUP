package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAddressRepository

class CreateAddressUseCase(private val repository: LevelUpAddressRepository) {

    suspend operator fun invoke(run: String, input: AddressInput): LevelUpResult<LevelUpAddress> {
        return repository.createAddress(run, input)
    }
}
