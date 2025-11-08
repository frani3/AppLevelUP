package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.domain.repository.SessionRepository

class SessionRepositoryImpl : SessionRepository {
    override fun isLoggedIn(): Boolean {
        // TODO: reemplazar por DataStore/Room cuando tengas login real
        return false
    }
}
