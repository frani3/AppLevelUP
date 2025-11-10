package com.applevelup.levepupgamerapp.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.favoriteDataStore: DataStore<Preferences> by preferencesDataStore(name = "favorite_preferences")

class FavoritePreferencesDataSource(context: Context) {

    private val dataStore = context.applicationContext.favoriteDataStore

    val favoriteIdsFlow: Flow<Set<Int>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[FAVORITE_IDS_KEY]
                ?.mapNotNull { value -> value.toIntOrNull() }
                ?.toSet()
                .orEmpty()
        }

    suspend fun setFavorite(productId: Int, isFavorite: Boolean) {
        dataStore.edit { preferences ->
            val current = preferences[FAVORITE_IDS_KEY].orEmpty().toMutableSet()
            val key = productId.toString()
            if (isFavorite) {
                current.add(key)
            } else {
                current.remove(key)
            }
            preferences[FAVORITE_IDS_KEY] = current
        }
    }

    suspend fun toggleFavorite(productId: Int) {
        dataStore.edit { preferences ->
            val current = preferences[FAVORITE_IDS_KEY].orEmpty().toMutableSet()
            val key = productId.toString()
            if (!current.add(key)) {
                current.remove(key)
            }
            preferences[FAVORITE_IDS_KEY] = current
        }
    }

    companion object {
        private val FAVORITE_IDS_KEY = stringSetPreferencesKey("favorite_product_ids")
    }
}
