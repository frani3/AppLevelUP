package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(navController: NavController) {

    // --- ESTADOS (Esto vendría de tu ViewModel) ---
    var allNotificationsEnabled by rememberSaveable { mutableStateOf(true) }
    var offersEnabled by rememberSaveable { mutableStateOf(true) }
    var newReleasesEnabled by rememberSaveable { mutableStateOf(true) }
    var orderUpdatesEnabled by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Notificaciones", fontWeight = FontWeight.Bold) },
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
                }
            )
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // --- Control Maestro ---
            item {
                NotificationSettingItem(
                    title = "Habilitar todas las notificaciones",
                    description = "Recibe alertas sobre todo lo relacionado a tu cuenta.",
                    checked = allNotificationsEnabled,
                    onCheckedChange = { allNotificationsEnabled = it }
                )
            }

            item {
                Divider(
                    color = Color.Gray.copy(alpha = 0.3f),
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                )
            }

            // --- Ajustes Granulares ---
            item {
                NotificationSettingItem(
                    title = "Ofertas y Promociones",
                    description = "Entérate primero de nuestros descuentos exclusivos.",
                    checked = offersEnabled,
                    onCheckedChange = { offersEnabled = it },
                    enabled = allNotificationsEnabled // Se desactiva si el maestro está apagado
                )
            }
            item {
                NotificationSettingItem(
                    title = "Nuevos Lanzamientos",
                    description = "Recibe notificaciones cuando lleguen nuevos productos.",
                    checked = newReleasesEnabled,
                    onCheckedChange = { newReleasesEnabled = it },
                    enabled = allNotificationsEnabled
                )
            }
            item {
                NotificationSettingItem(
                    title = "Actualizaciones de Pedido",
                    description = "Sigue el estado de tus compras en tiempo real.",
                    checked = orderUpdatesEnabled,
                    onCheckedChange = { orderUpdatesEnabled = it },
                    enabled = allNotificationsEnabled
                )
            }
        }
    }
}

// --- COMPONENTE REUTILIZABLE PARA CADA AJUSTE ---
@Composable
fun NotificationSettingItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    val textColor = if (enabled) Color.White else Color.Gray
    val descriptionColor = if (enabled) Color.Gray else Color.DarkGray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold, color = textColor, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, color = descriptionColor, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PrimaryPurple,
                checkedTrackColor = PrimaryPurple.copy(alpha = 0.5f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray,
                disabledCheckedThumbColor = PrimaryPurple.copy(alpha = 0.5f),
                disabledUncheckedThumbColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
    }
}


