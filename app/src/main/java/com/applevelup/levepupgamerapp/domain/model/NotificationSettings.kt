package com.applevelup.levepupgamerapp.domain.model

data class NotificationSettings(
    val allEnabled: Boolean = true,
    val offersEnabled: Boolean = true,
    val newReleasesEnabled: Boolean = true,
    val orderUpdatesEnabled: Boolean = true
)
