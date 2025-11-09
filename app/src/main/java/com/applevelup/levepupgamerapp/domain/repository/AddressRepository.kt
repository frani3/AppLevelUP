package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.Address

interface AddressRepository {
    fun getAddresses(): List<Address>
    fun addAddress(
        alias: String,
        street: String,
        city: String,
        details: String,
        setAsDefault: Boolean
    ): Address
    fun deleteAddress(id: Int)
    fun setDefaultAddress(id: Int)
}
