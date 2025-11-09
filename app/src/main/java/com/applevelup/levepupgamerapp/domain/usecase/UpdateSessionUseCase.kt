package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.SessionState
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository

class UpdateSessionUseCase(private val repository: SessionRepository) {
    suspend operator fun invoke(transform: (SessionState) -> SessionState) {
        repository.updateSession(transform)
    }
}
