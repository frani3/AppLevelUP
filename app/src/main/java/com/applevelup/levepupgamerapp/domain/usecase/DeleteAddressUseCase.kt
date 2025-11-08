package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.AddressRepository

class DeleteAddressUseCase(private val repo: AddressRepository) {
    operator fun invoke(id: Int) = repo.deleteAddress(id)
}
