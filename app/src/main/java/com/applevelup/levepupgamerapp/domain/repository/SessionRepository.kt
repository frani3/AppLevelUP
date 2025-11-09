package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.SessionState
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun observeSession(): Flow<SessionState>
    suspend fun getSession(): SessionState
    suspend fun saveSession(state: SessionState)
    suspend fun updateSession(transform: (SessionState) -> SessionState)
    suspend fun clearSession()
}
