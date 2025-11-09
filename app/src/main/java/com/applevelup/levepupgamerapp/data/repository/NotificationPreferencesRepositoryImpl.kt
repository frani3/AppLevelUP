package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.prefs.NotificationPreferencesDataSource
import com.applevelup.levepupgamerapp.domain.model.NotificationSettings
import com.applevelup.levepupgamerapp.domain.repository.NotificationPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NotificationPreferencesRepositoryImpl(
    private val prefs: NotificationPreferencesDataSource = LevelUpApplication.notificationPreferences
) : NotificationPreferencesRepository {

    override fun observeSettings(): Flow<NotificationSettings> = prefs.settingsFlow

    override suspend fun getSettings(): NotificationSettings = prefs.settingsFlow.first()

    override suspend fun saveSettings(settings: NotificationSettings) {
        prefs.save(settings)
    }

    override suspend fun updateSettings(transform: (NotificationSettings) -> NotificationSettings) {
        prefs.update(transform)
    }
}
