package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.repository.AddressRepository

class GetAddressesUseCase(private val repo: AddressRepository) {
    operator fun invoke(): List<Address> = repo.getAddresses()
}
