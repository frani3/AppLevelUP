package com.applevelup.levepupgamerapp

import android.app.Application
import com.applevelup.levepupgamerapp.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LevelUpApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val appDatabase: AppDatabase by lazy { AppDatabase.build(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val database = appDatabase
        applicationScope.launch {
            database.seed()
        }
    }

    companion object {
        lateinit var instance: LevelUpApplication
            private set

        val database: AppDatabase
            get() = instance.appDatabase
    }
}
