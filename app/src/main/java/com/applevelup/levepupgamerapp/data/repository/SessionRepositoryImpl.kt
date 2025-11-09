package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.prefs.SessionPreferencesDataSource
import com.applevelup.levepupgamerapp.domain.model.SessionState
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SessionRepositoryImpl(
    private val prefs: SessionPreferencesDataSource = LevelUpApplication.sessionPreferences
) : SessionRepository {

    override fun observeSession(): Flow<SessionState> = prefs.sessionFlow

    override suspend fun getSession(): SessionState = prefs.sessionFlow.first()

    override suspend fun saveSession(state: SessionState) {
        prefs.saveSession(state)
    }

    override suspend fun updateSession(transform: (SessionState) -> SessionState) {
        prefs.updateSession(transform)
    }

    override suspend fun clearSession() {
        prefs.clearSession()
    }
}
