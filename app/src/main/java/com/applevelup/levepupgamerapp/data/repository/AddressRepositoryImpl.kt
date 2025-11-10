package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.repository.AddressRepository
import kotlinx.coroutines.runBlocking

class AddressRepositoryImpl(
    private val userRepository: UserRepositoryImpl = UserRepositoryImpl()
) : AddressRepository {

    override fun getAddresses(): List<Address> = synchronized(addresses) {
        if (addresses.isEmpty()) {
            bootstrapFromProfileLocked()
        }
        addresses.toList()
    }

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
        updateNextIdLocked()

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
                updateNextIdLocked()
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

    fun setPrimaryAddress(fullAddress: String) {
        synchronized(addresses) {
            addresses.clear()
            if (fullAddress.isNotBlank()) {
                addresses.add(
                    Address(
                        id = nextId++,
                        alias = "Principal",
                        street = fullAddress.trim(),
                        city = "",
                        details = "",
                        isDefault = true
                    )
                )
            }
            updateNextIdLocked()
        }
    }

    fun clearAll() {
        synchronized(addresses) {
            addresses.clear()
            nextId = 1
        }
    }

    private fun bootstrapFromProfileLocked() {
        val profile = runBlocking { userRepository.getUserProfile() }
        val mainAddress = profile?.address?.trim().orEmpty()
        if (mainAddress.isEmpty()) {
            updateNextIdLocked()
            return
        }
        addresses.add(
            Address(
                id = nextId++,
                alias = "Principal",
                street = mainAddress,
                city = profile?.comuna?.trim().orEmpty(),
                details = "",
                isDefault = true
            )
        )
        updateNextIdLocked()
    }

    private fun updateNextIdLocked() {
        nextId = (addresses.maxOfOrNull { it.id } ?: 0) + 1
    }

    companion object {
        private val addresses = mutableListOf<Address>()
        private var nextId: Int = 1
    }
}
