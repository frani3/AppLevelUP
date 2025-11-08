package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.NotificationSettings

interface NotificationPreferencesRepository {
    fun getSettings(): NotificationSettings
    fun saveSettings(settings: NotificationSettings)
}
