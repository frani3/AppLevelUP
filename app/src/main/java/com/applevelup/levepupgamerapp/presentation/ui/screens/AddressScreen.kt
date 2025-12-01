package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.components.AddressCard
import com.applevelup.levepupgamerapp.presentation.ui.components.EmptyAddressView
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.ui.theme.TopBarAndDrawerColor
import com.applevelup.levepupgamerapp.presentation.viewmodel.levelup.LevelUpAddressViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.levelup.LevelUpAuthViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.levelup.LevelUpViewModelFactory
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    navController: NavController
) {
    val factory = remember { LevelUpViewModelFactory() }
    val addressViewModel: LevelUpAddressViewModel = viewModel(factory = factory)
    val authViewModel: LevelUpAuthViewModel = viewModel(factory = factory)
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val profileState by authViewModel.profileState.collectAsState()
    val run = (profileState as? LevelUpResource.Success)?.data?.run

    val addressesFlow: StateFlow<LevelUpResource<List<LevelUpAddress>>>? = remember(run) {
        run?.let { addressViewModel.observeAddresses(it) }
    }
    val addressesResource = addressesFlow
        ?.collectAsState(initial = LevelUpResource.Loading)
        ?.value ?: LevelUpResource.Loading

    DisposableEffect(lifecycleOwner, run) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && run != null) {
                addressViewModel.refreshAddresses(run)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(run) {
        if (run != null) {
            addressViewModel.refreshAddresses(run)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Direcciones de Envío", fontWeight = FontWeight.Bold) },
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Destinations.AddAddress.route) },
                containerColor = PrimaryPurple,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar dirección")
            }
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val profileResource = profileState) {
                LevelUpResource.Loading -> LoadingView()
                is LevelUpResource.Error -> ErrorView(
                    message = "No pudimos cargar tu perfil",
                    onRetry = {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Intenta nuevamente más tarde") }
                    }
                )
                is LevelUpResource.Success -> {
                    val profile = profileResource.data
                    val currentRun = profile?.run
                    if (currentRun.isNullOrBlank()) {
                        ErrorView(message = "Tu cuenta no tiene RUN asociado.")
                    } else {
                        AddressListContent(
                            resource = addressesResource,
                            profile = profile,
                            onRetry = { addressViewModel.refreshAddresses(currentRun) },
                            onSetPrimary = { address ->
                                coroutineScope.launch {
                                    when (val result = addressViewModel.setPrimaryAddress(currentRun, address.id)) {
                                        is LevelUpResult.Success -> {
                                            // Forzar refresh para actualizar la UI
                                            addressViewModel.refreshAddresses(currentRun, forceRefresh = true)
                                            snackbarHostState.showSnackbar("Dirección marcada como principal")
                                        }
                                        is LevelUpResult.Failure -> {
                                            snackbarHostState.showSnackbar(result.throwable.message ?: "Error al marcar como principal")
                                        }
                                    }
                                }
                            },
                            onDelete = { address ->
                                coroutineScope.launch {
                                    when (val result = addressViewModel.deleteAddress(currentRun, address.id)) {
                                        is LevelUpResult.Success -> snackbarHostState.showSnackbar("Dirección eliminada")
                                        is LevelUpResult.Failure -> snackbarHostState.showSnackbar(
                                            result.throwable.message ?: "Error al eliminar"
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddressListContent(
    resource: LevelUpResource<List<LevelUpAddress>>,
    profile: LevelUpUserProfile?,
    onRetry: () -> Unit,
    onSetPrimary: (LevelUpAddress) -> Unit,
    onDelete: (LevelUpAddress) -> Unit
) {
    // Crear dirección desde el perfil si tiene datos
    val profileAddress: LevelUpAddress? = if (
        !profile?.address.isNullOrBlank() && 
        !profile?.commune.isNullOrBlank() && 
        !profile?.region.isNullOrBlank()
    ) {
        LevelUpAddress(
            id = "profile-address",
            fullName = profile?.name ?: "Usuario",
            line1 = profile?.address ?: "",
            city = profile?.commune ?: "",
            region = profile?.region ?: "",
            country = "Chile",
            isPrimary = true
        )
    } else null

    when (resource) {
        LevelUpResource.Loading -> LoadingView()
        is LevelUpResource.Error -> {
            // Si hay error pero tenemos dirección del perfil, mostrarla
            if (profileAddress != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ProfileAddressCard(address = profileAddress)
                    }
                }
            } else {
                ErrorView(
                    message = "No pudimos obtener tus direcciones",
                    onRetry = onRetry
                )
            }
        }
        is LevelUpResource.Success -> {
            val addresses = resource.data
            if (addresses.isEmpty() && profileAddress == null) {
                EmptyAddressView()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Mostrar dirección del perfil primero si existe
                    if (profileAddress != null) {
                        item(key = "profile-address") {
                            ProfileAddressCard(address = profileAddress)
                        }
                    }
                    
                    // Mostrar direcciones adicionales del endpoint
                    items(addresses, key = { it.id }) { address ->
                        AddressCard(
                            address = address,
                            onSelect = { },
                            onDelete = { onDelete(address) },
                            onSetPrimary = { onSetPrimary(address) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileAddressCard(address: LevelUpAddress) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, PrimaryPurple, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = PrimaryPurple,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = address.fullName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                
                // Badge "PRINCIPAL · DATOS DE PERFIL"
                Surface(
                    color = PrimaryPurple.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "PRINCIPAL · DATOS DE PERFIL",
                        color = PrimaryPurple,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(address.line1, color = Color.LightGray, fontSize = 14.sp)
            Text("${address.city}, ${address.region}", color = Color.Gray, fontSize = 13.sp)
            Text(address.country, color = Color.Gray, fontSize = 13.sp)
        }
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = PrimaryPurple)
    }
}

@Composable
private fun ErrorView(
    message: String,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, color = Color.White, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
        onRetry?.let {
            Button(
                onClick = it,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Text("Reintentar", color = Color.White)
            }
        }
    }
}
