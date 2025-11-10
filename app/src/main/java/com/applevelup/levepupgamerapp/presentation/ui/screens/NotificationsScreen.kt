package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.NotificationCategory
import com.applevelup.levepupgamerapp.domain.model.UserNotification
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.ui.theme.TopBarAndDrawerColor
import com.applevelup.levepupgamerapp.presentation.viewmodel.NotificationFeedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModelStoreOwner = remember(context) {
        (context as? ComponentActivity)
            ?: error("NotificationsScreen requiere un ComponentActivity como host")
    }
    val viewModel: NotificationFeedViewModel = viewModel(viewModelStoreOwner)
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val unreadSuffix = if (state.unreadCount > 0) " (${state.unreadCount})" else ""
                    Text("Notificaciones$unreadSuffix", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (state.unreadCount > 0) {
                        TextButton(onClick = viewModel::markAllAsRead) {
                            Text("Marcar leídas", color = PrimaryPurple)
                        }
                    }
                    IconButton(onClick = { navController.navigate(Destinations.NotificationSettings.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Configuración",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = PureBlackBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.notifications.isEmpty()) {
            NotificationEmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                onGoToSettings = { navController.navigate(Destinations.NotificationSettings.route) }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = state.notifications, key = { it.id }) { notification ->
                    NotificationCard(
                        notification = notification,
                        onToggleRead = {
                            viewModel.toggleRead(notification.id)
                            if (!notification.isRead) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Marcada como leída")
                                }
                            }
                        },
                        onDismiss = {
                            viewModel.removeNotification(notification.id)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Notificación ocultada")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: UserNotification,
    onToggleRead: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (notification.isRead) Color(0xFF0F0F14) else Color(0xFF16161F)
    val borderColor = if (notification.isRead) Color.Transparent else PrimaryPurple.copy(alpha = 0.35f)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(20.dp),
        border = if (borderColor == Color.Transparent) null else BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NotificationLeadingIcon(notification)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = notification.message,
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = notification.timestamp,
                        color = Color.Gray.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelSmall
                    )
                    AnimatedVisibility(visible = !notification.isRead, enter = fadeIn(), exit = fadeOut()) {
                        Badge(containerColor = PrimaryPurple, modifier = Modifier.padding(top = 8.dp)) {
                            Text("Nuevo", fontSize = 10.sp, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                CategoryChip(category = notification.category)
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onToggleRead, colors = ButtonDefaults.textButtonColors(contentColor = PrimaryPurple)) {
                    Text(if (notification.isRead) "Marcar como no leída" else "Marcar como leída")
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Descartar",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationLeadingIcon(notification: UserNotification) {
    val background = when (notification.category) {
        NotificationCategory.Order -> Color(0xFF1B5E20)
        NotificationCategory.Promotion -> Color(0xFF6A1B9A)
        NotificationCategory.Loyalty -> Color(0xFF0D47A1)
        NotificationCategory.Reminder -> Color(0xFFBF360C)
        NotificationCategory.Community -> Color(0xFF006064)
        NotificationCategory.Support -> Color(0xFF4E342E)
        NotificationCategory.General -> Color(0xFF263238)
    }
    val icon = iconForCategory(notification.category)

    BadgedBox(
        badge = {
            AnimatedVisibility(visible = !notification.isRead, enter = fadeIn(), exit = fadeOut()) {
                Badge(containerColor = PrimaryPurple, modifier = Modifier.offset(x = 6.dp, y = (-2).dp)) {}
            }
        }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(background.copy(alpha = 0.85f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
private fun CategoryChip(category: NotificationCategory) {
    val label = when (category) {
        NotificationCategory.Order -> "Pedido"
        NotificationCategory.Promotion -> "Promoción"
        NotificationCategory.Loyalty -> "Recompensa"
        NotificationCategory.Reminder -> "Recordatorio"
        NotificationCategory.Community -> "Comunidad"
        NotificationCategory.Support -> "Soporte"
        NotificationCategory.General -> "General"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun NotificationEmptyState(
    modifier: Modifier = Modifier,
    onGoToSettings: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.NotificationsActive,
                contentDescription = null,
                tint = PrimaryPurple,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Estás al día",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cuando tengas novedades sobre tus pedidos, promociones o recompensas las verás aquí.",
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = onGoToSettings) {
            Text("Configurar notificaciones", color = PrimaryPurple)
        }
    }
}

private fun iconForCategory(category: NotificationCategory): ImageVector = when (category) {
    NotificationCategory.Order -> Icons.Filled.ShoppingCart
    NotificationCategory.Promotion -> Icons.Filled.Star
    NotificationCategory.Loyalty -> Icons.Filled.Timeline
    NotificationCategory.Reminder -> Icons.Filled.WarningAmber
    NotificationCategory.Community -> Icons.Filled.Notifications
    NotificationCategory.Support -> Icons.Filled.SupportAgent
    NotificationCategory.General -> Icons.Filled.Notifications
}
