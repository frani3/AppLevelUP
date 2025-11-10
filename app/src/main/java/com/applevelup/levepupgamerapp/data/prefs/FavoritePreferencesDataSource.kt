package com.applevelup.levepupgamerapp.data.prefs

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.Locale

private val Context.favoriteDataStore: DataStore<Preferences> by preferencesDataStore(name = "favorite_preferences")

class FavoritePreferencesDataSource(context: Context) {

    private val dataStore = context.applicationContext.favoriteDataStore

    fun favoriteIdsFlow(userKey: String): Flow<Set<Int>> {
        val key = prefsKeyFor(userKey)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    Log.e(TAG, "Error leyendo favoritos", exception)
                    emit(emptyPreferences())
                }
            }
            .map { preferences ->
                preferences[key]
                    ?.mapNotNull { value -> value.toIntOrNull() }
                    ?.toSet()
                    .orEmpty()
            }
    }

    suspend fun currentFavorites(userKey: String): Set<Int> {
        return try {
            favoriteIdsFlow(userKey).first()
        } catch (error: Exception) {
            Log.e(TAG, "No se pudo obtener favoritos actuales", error)
            emptySet()
        }
    }

    suspend fun setFavorite(userKey: String, productId: Int, isFavorite: Boolean) {
        val key = prefsKeyFor(userKey)
        runCatching {
            dataStore.edit { preferences ->
                val current = preferences[key].orEmpty().toMutableSet()
                val entry = productId.toString()
                if (isFavorite) {
                    current.add(entry)
                } else {
                    current.remove(entry)
                }
                preferences[key] = current.toSet()
            }
        }.onFailure { error ->
            Log.e(TAG, "No se pudo actualizar favoritos para $userKey", error)
        }
    }

    suspend fun toggleFavorite(userKey: String, productId: Int) {
        val key = prefsKeyFor(userKey)
        runCatching {
            dataStore.edit { preferences ->
                val current = preferences[key].orEmpty().toMutableSet()
                val entry = productId.toString()
                if (!current.add(entry)) {
                    current.remove(entry)
                }
                preferences[key] = current.toSet()
            }
        }.onFailure { error ->
            Log.e(TAG, "No se pudo alternar favorito para $userKey", error)
        }
    }

    private fun prefsKeyFor(rawKey: String): Preferences.Key<Set<String>> {
        val sanitized = sanitize(rawKey)
        return stringSetPreferencesKey("favorite_product_ids_$sanitized")
    }

    private fun sanitize(value: String): String {
        val normalized = value.lowercase(Locale.ROOT)
        return normalized.replace("[^a-z0-9_]".toRegex(), "_")
    }

    companion object {
        private const val TAG = "FavoritePrefs"
    }
}
