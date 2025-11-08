package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.Address

interface AddressRepository {
    fun getAddresses(): List<Address>
    fun deleteAddress(id: Int)
}
