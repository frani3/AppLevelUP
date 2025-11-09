package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.UserRepository

class UpdateUserProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(fullName: String, email: String, newPassword: String?) {
        repository.updateUser(fullName, email, newPassword)
    }
}
