package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.SessionState
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository

class GetSessionUseCase(private val repository: SessionRepository) {
    suspend operator fun invoke(): SessionState = repository.getSession()
}
