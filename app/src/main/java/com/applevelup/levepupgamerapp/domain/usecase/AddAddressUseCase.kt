package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.repository.AddressRepository

class AddAddressUseCase(private val repository: AddressRepository) {
    operator fun invoke(
        alias: String,
        street: String,
        city: String,
        details: String,
        setAsDefault: Boolean
    ): Address {
        return repository.addAddress(alias, street, city, details, setAsDefault)
    }
}
