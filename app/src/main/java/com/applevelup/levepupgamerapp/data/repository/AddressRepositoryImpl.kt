package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.repository.AddressRepository

class AddressRepositoryImpl : AddressRepository {

    private val addresses = mutableListOf(
        Address(1, "Casa", "Av. Siempre Viva 742", "Springfield", "Portón Verde", true),
        Address(2, "Oficina", "Calle Falsa 123", "Shelbyville", "Piso 3, oficina B", false)
    )

    override fun getAddresses(): List<Address> = addresses.toList()

    override fun deleteAddress(id: Int) {
        addresses.removeAll { it.id == id }
    }
}
