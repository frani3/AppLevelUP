package com.applevelup.levepupgamerapp.domain.model

import androidx.annotation.DrawableRes

/** Represents a notification to display to the user inside the in-app inbox. */
data class UserNotification(
    val id: Int,
    val title: String,
    val message: String,
    val timestamp: String,
    val category: NotificationCategory = NotificationCategory.General,
    val isRead: Boolean = false,
    val actionLabel: String? = null,
    @DrawableRes val imageRes: Int? = null
)

enum class NotificationCategory {
    Order,
    Promotion,
    Loyalty,
    Reminder,
    Community,
    Support,
    General
}
