package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.repository.AddressRepository

class AddressRepositoryImpl : AddressRepository {

    override fun getAddresses(): List<Address> = synchronized(addresses) { addresses.toList() }

    override fun addAddress(
        alias: String,
        street: String,
        city: String,
        details: String,
        setAsDefault: Boolean
    ): Address = synchronized(addresses) {
        val id = nextId++
        val trimmedAddress = Address(
            id = id,
            alias = alias.trim(),
            street = street.trim(),
            city = city.trim(),
            details = details.trim(),
            isDefault = false
        )

        addresses.add(0, trimmedAddress)

        if (setAsDefault || addresses.none { it.isDefault }) {
            setDefaultLocked(id)
        }

        addresses.first { it.id == id }
    }

    override fun deleteAddress(id: Int) {
        synchronized(addresses) {
            val removedWasDefault = addresses.firstOrNull { it.id == id }?.isDefault == true
            addresses.removeAll { it.id == id }
            if (addresses.isEmpty()) {
                return
            }
            if (removedWasDefault || addresses.none { it.isDefault }) {
                setDefaultLocked(addresses.first().id)
            }
        }
    }

    override fun setDefaultAddress(id: Int) {
        synchronized(addresses) {
            if (addresses.none { it.id == id }) return
            setDefaultLocked(id)
        }
    }

    private fun setDefaultLocked(id: Int) {
        var found = false
        for (index in addresses.indices) {
            val current = addresses[index]
            val shouldBeDefault = current.id == id
            if (shouldBeDefault) found = true
            if (current.isDefault != shouldBeDefault) {
                addresses[index] = current.copy(isDefault = shouldBeDefault)
            }
        }
        if (!found && addresses.isNotEmpty()) {
            val first = addresses.first()
            if (!first.isDefault) {
                addresses[0] = first.copy(isDefault = true)
            }
        }
    }

    companion object {
        private val addresses = mutableListOf(
            Address(1, "Casa", "Av. Siempre Viva 742", "Springfield", "Portón Verde", true),
            Address(2, "Oficina", "Calle Falsa 123", "Shelbyville", "Piso 3, oficina B", false)
        )

        private var nextId: Int = (addresses.maxOfOrNull { it.id } ?: 0) + 1
    }
}
