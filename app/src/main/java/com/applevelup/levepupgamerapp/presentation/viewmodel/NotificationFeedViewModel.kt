package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.applevelup.levepupgamerapp.domain.model.NotificationCategory
import com.applevelup.levepupgamerapp.domain.model.UserNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class NotificationFeedUiState(
    val notifications: List<UserNotification> = emptyList(),
    val unreadCount: Int = 0
)

class NotificationFeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        NotificationFeedUiState(
            notifications = sampleNotifications()
        ).recalculateUnread()
    )
    val uiState: StateFlow<NotificationFeedUiState> = _uiState

    fun markAsRead(id: Int) {
        _uiState.update { current ->
            current.copy(
                notifications = current.notifications.map { notification ->
                    if (notification.id == id) notification.copy(isRead = true) else notification
                }
            ).recalculateUnread()
        }
    }

    fun toggleRead(id: Int) {
        _uiState.update { current ->
            current.copy(
                notifications = current.notifications.map { notification ->
                    if (notification.id == id) notification.copy(isRead = !notification.isRead) else notification
                }
            ).recalculateUnread()
        }
    }

    fun markAllAsRead() {
        _uiState.update { current ->
            current.copy(
                notifications = current.notifications.map { it.copy(isRead = true) }
            ).recalculateUnread()
        }
    }

    fun removeNotification(id: Int) {
        _uiState.update { current ->
            current.copy(
                notifications = current.notifications.filterNot { it.id == id }
            ).recalculateUnread()
        }
    }

    private fun NotificationFeedUiState.recalculateUnread(): NotificationFeedUiState {
        val unread = notifications.count { !it.isRead }
        return copy(unreadCount = unread)
    }

    private fun sampleNotifications(): List<UserNotification> = listOf(
        UserNotification(
            id = 1,
            title = "Tu pedido está en camino",
            message = "El pedido #LV-2315 fue despachado. Podrás hacer seguimiento en la sección de pedidos.",
            timestamp = "Hace 2 h",
            category = NotificationCategory.Order,
            isRead = false
        ),
        UserNotification(
            id = 2,
            title = "¡Oferta relámpago en teclados!",
            message = "Aprovecha un 25% de descuento en teclados mecánicos seleccionados hasta medianoche.",
            timestamp = "Hace 5 h",
            category = NotificationCategory.Promotion,
            isRead = false
        ),
        UserNotification(
            id = 3,
            title = "Nueva reseña en Catan",
            message = "Francisca comentó: 'Llegó el mismo día, excelente servicio.'",
            timestamp = "Ayer",
            category = NotificationCategory.Community,
            isRead = true
        ),
        UserNotification(
            id = 4,
            title = "Recompensa LevelUp",
            message = "Ganaste 120 XP por completar tu perfil. Canjéalos por cupones y beneficios exclusivos.",
            timestamp = "Ayer",
            category = NotificationCategory.Loyalty,
            isRead = false
        ),
        UserNotification(
            id = 5,
            title = "Recordatorio de carrito",
            message = "Tienes una Nintendo Switch OLED esperándote. ¡No dejes que se agote!",
            timestamp = "Hace 3 días",
            category = NotificationCategory.Reminder,
            isRead = true
        ),
        UserNotification(
            id = 6,
            title = "Nuevo soporte disponible",
            message = "Nuestro equipo de soporte respondió tu caso. Revisa los detalles para continuar.",
            timestamp = "Hace 4 días",
            category = NotificationCategory.Support,
            isRead = true
        )
    )
}
