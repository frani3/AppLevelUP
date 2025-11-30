package com.applevelup.levepupgamerapp.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.applevelup.levepupgamerapp.domain.model.SessionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session_preferences")

class SessionPreferencesDataSource(context: Context) {

    private val dataStore = context.applicationContext.sessionDataStore

    val sessionFlow: Flow<SessionState> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            SessionState(
                isLoggedIn = preferences[KEY_LOGGED_IN] ?: false,
                userId = preferences[KEY_USER_ID],
                email = preferences[KEY_EMAIL],
                fullName = preferences[KEY_FULL_NAME],
                rememberMe = preferences[KEY_REMEMBER_ME] ?: false,
                profileRole = preferences[KEY_PROFILE_ROLE],
                isSuperAdmin = preferences[KEY_IS_SUPER_ADMIN] ?: false
            )
        }

    val tokenFlow: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences -> preferences[KEY_AUTH_TOKEN] }

    suspend fun saveSession(state: SessionState) {
        dataStore.edit { preferences ->
            preferences[KEY_LOGGED_IN] = state.isLoggedIn

            if (state.userId != null) {
                preferences[KEY_USER_ID] = state.userId
            } else {
                preferences.remove(KEY_USER_ID)
            }

            if (state.email.isNullOrBlank()) {
                preferences.remove(KEY_EMAIL)
            } else {
                preferences[KEY_EMAIL] = state.email
            }

            if (state.fullName.isNullOrBlank()) {
                preferences.remove(KEY_FULL_NAME)
            } else {
                preferences[KEY_FULL_NAME] = state.fullName
            }

            preferences[KEY_REMEMBER_ME] = state.rememberMe
            if (state.profileRole.isNullOrBlank()) {
                preferences.remove(KEY_PROFILE_ROLE)
            } else {
                preferences[KEY_PROFILE_ROLE] = state.profileRole
            }
            preferences[KEY_IS_SUPER_ADMIN] = state.isSuperAdmin
        }
    }

    suspend fun updateSession(transform: (SessionState) -> SessionState) {
        val current = sessionFlow.first()
        saveSession(transform(current))
    }

    suspend fun clearSession() {
        val current = sessionFlow.first()
        saveSession(
            SessionState(
                isLoggedIn = false,
                userId = null,
                email = if (current.rememberMe) current.email else null,
                fullName = null,
                rememberMe = current.rememberMe,
                profileRole = null,
                isSuperAdmin = false
            )
        )
    }

    suspend fun saveToken(token: String?) {
        dataStore.edit { preferences ->
            if (token.isNullOrBlank()) {
                preferences.remove(KEY_AUTH_TOKEN)
            } else {
                preferences[KEY_AUTH_TOKEN] = token
            }
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_AUTH_TOKEN)
        }
    }

    companion object {
        private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
        private val KEY_USER_ID = longPreferencesKey("user_id")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_FULL_NAME = stringPreferencesKey("full_name")
        private val KEY_REMEMBER_ME = booleanPreferencesKey("remember_me")
        private val KEY_PROFILE_ROLE = stringPreferencesKey("profile_role")
        private val KEY_IS_SUPER_ADMIN = booleanPreferencesKey("is_super_admin")
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
    }
}
