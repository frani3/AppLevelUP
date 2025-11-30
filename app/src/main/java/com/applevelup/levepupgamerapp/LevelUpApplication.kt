package com.applevelup.levepupgamerapp

import android.app.Application
import com.applevelup.levepupgamerapp.data.prefs.FavoritePreferencesDataSource
import com.applevelup.levepupgamerapp.data.prefs.NotificationPreferencesDataSource
import com.applevelup.levepupgamerapp.data.local.AppDatabase
import com.applevelup.levepupgamerapp.data.prefs.SessionPreferencesDataSource

class LevelUpApplication : Application() {

    val appDatabase: AppDatabase by lazy { AppDatabase.build(this) }
    val sessionPreferencesDataSource: SessionPreferencesDataSource by lazy { SessionPreferencesDataSource(this) }
    val notificationPreferencesDataSource: NotificationPreferencesDataSource by lazy { NotificationPreferencesDataSource(this) }
    val favoritePreferencesDataSource: FavoritePreferencesDataSource by lazy { FavoritePreferencesDataSource(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: LevelUpApplication
            private set

        val database: AppDatabase
            get() = instance.appDatabase

        val sessionPreferences: SessionPreferencesDataSource
            get() = instance.sessionPreferencesDataSource

        val notificationPreferences: NotificationPreferencesDataSource
            get() = instance.notificationPreferencesDataSource

        val favoritePreferences: FavoritePreferencesDataSource
            get() = instance.favoritePreferencesDataSource
    }
}
