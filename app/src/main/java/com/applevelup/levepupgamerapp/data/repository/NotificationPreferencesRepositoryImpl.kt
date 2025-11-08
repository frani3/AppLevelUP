package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.domain.model.NotificationSettings
import com.applevelup.levepupgamerapp.domain.repository.NotificationPreferencesRepository

class NotificationPreferencesRepositoryImpl : NotificationPreferencesRepository {

    // Temporal: una versión final usar DataStore
    private var currentSettings = NotificationSettings()

    override fun getSettings(): NotificationSettings = currentSettings

    override fun saveSettings(settings: NotificationSettings) {
        currentSettings = settings
    }
}
