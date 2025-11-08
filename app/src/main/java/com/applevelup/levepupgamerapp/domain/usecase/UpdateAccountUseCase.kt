package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.UserRepository

class UpdateAccountUseCase(private val repo: UserRepository) {
    operator fun invoke(fullName: String, email: String, newPassword: String?) {
        repo.updateUser(fullName, email, newPassword)
    }
}
