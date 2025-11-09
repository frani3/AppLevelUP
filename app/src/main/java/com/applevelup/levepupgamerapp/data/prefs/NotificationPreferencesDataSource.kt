package com.applevelup.levepupgamerapp.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.applevelup.levepupgamerapp.domain.model.NotificationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.notificationDataStore: DataStore<Preferences> by preferencesDataStore(name = "notification_preferences")

class NotificationPreferencesDataSource(context: Context) {

    private val dataStore = context.applicationContext.notificationDataStore

    val settingsFlow: Flow<NotificationSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            NotificationSettings(
                allEnabled = preferences[KEY_ALL_ENABLED] ?: true,
                offersEnabled = preferences[KEY_OFFERS_ENABLED] ?: true,
                newReleasesEnabled = preferences[KEY_NEW_RELEASES_ENABLED] ?: true,
                orderUpdatesEnabled = preferences[KEY_ORDER_UPDATES_ENABLED] ?: true
            )
        }

    suspend fun save(settings: NotificationSettings) {
        dataStore.edit { preferences ->
            preferences[KEY_ALL_ENABLED] = settings.allEnabled
            preferences[KEY_OFFERS_ENABLED] = settings.offersEnabled
            preferences[KEY_NEW_RELEASES_ENABLED] = settings.newReleasesEnabled
            preferences[KEY_ORDER_UPDATES_ENABLED] = settings.orderUpdatesEnabled
        }
    }

    suspend fun update(transform: (NotificationSettings) -> NotificationSettings) {
        save(transform(settingsFlow.first()))
    }

    companion object {
        private val KEY_ALL_ENABLED = booleanPreferencesKey("notifications_all")
        private val KEY_OFFERS_ENABLED = booleanPreferencesKey("notifications_offers")
        private val KEY_NEW_RELEASES_ENABLED = booleanPreferencesKey("notifications_new_releases")
        private val KEY_ORDER_UPDATES_ENABLED = booleanPreferencesKey("notifications_order_updates")
    }
}
