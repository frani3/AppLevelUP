package com.applevelup.levepupgamerapp.presentation.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.components.NotificationSettingItem
import com.applevelup.levepupgamerapp.presentation.ui.theme.*
import com.applevelup.levepupgamerapp.presentation.viewmodel.NotificationsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        coroutineScope.launch {
            if (granted) {
                viewModel.toggleAll(true)
                snackbarHostState.showSnackbar("Notificaciones habilitadas")
            } else {
                snackbarHostState.showSnackbar("Permiso de notificaciones denegado")
            }
        }
    }

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
        containerColor = PureBlackBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                NotificationSettingItem(
                    title = "Habilitar todas las notificaciones",
                    description = "Recibe alertas sobre todo lo relacionado a tu cuenta.",
                    checked = state.allEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val granted = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED
                                if (granted) {
                                    viewModel.toggleAll(true)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Notificaciones habilitadas")
                                    }
                                } else {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            } else {
                                viewModel.toggleAll(true)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Notificaciones habilitadas")
                                }
                            }
                        } else {
                            viewModel.toggleAll(false)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Notificaciones desactivadas")
                            }
                        }
                    }
                )
            }

            item {
                HorizontalDivider(
                    color = Color.Gray.copy(alpha = 0.3f),
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                )
            }

            item {
                NotificationSettingItem(
                    title = "Ofertas y Promociones",
                    description = "Entérate primero de nuestros descuentos exclusivos.",
                    checked = state.offersEnabled,
                    onCheckedChange = viewModel::toggleOffers,
                    enabled = state.allEnabled
                )
            }

            item {
                NotificationSettingItem(
                    title = "Nuevos Lanzamientos",
                    description = "Recibe notificaciones cuando lleguen nuevos productos.",
                    checked = state.newReleasesEnabled,
                    onCheckedChange = viewModel::toggleNewReleases,
                    enabled = state.allEnabled
                )
            }

            item {
                NotificationSettingItem(
                    title = "Actualizaciones de Pedido",
                    description = "Sigue el estado de tus compras en tiempo real.",
                    checked = state.orderUpdatesEnabled,
                    onCheckedChange = viewModel::toggleOrderUpdates,
                    enabled = state.allEnabled
                )
            }
        }
    }
}
