package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
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
            when (profileState) {
                LevelUpResource.Loading -> LoadingView()
                is LevelUpResource.Error -> ErrorView(
                    message = "No pudimos cargar tu perfil",
                    onRetry = {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Intenta nuevamente más tarde") }
                    }
                )
                is LevelUpResource.Success -> {
                    val profile = profileState.data
                    val currentRun = profile?.run
                    if (currentRun.isNullOrBlank()) {
                        ErrorView(message = "Tu cuenta no tiene RUN asociado.")
                    } else {
                        AddressListContent(
                            resource = addressesResource,
                            onRetry = { addressViewModel.refreshAddresses(currentRun) },
                            onSelect = { address ->
                                coroutineScope.launch {
                                    when (val result = addressViewModel.setPrimaryAddress(currentRun, address.id)) {
                                        is LevelUpResult.Success -> {
                                            snackbarHostState.showSnackbar("Dirección seleccionada")
                                            navController.popBackStack()
                                        }
                                        is LevelUpResult.Failure -> {
                                            snackbarHostState.showSnackbar(result.throwable.message ?: "Error al seleccionar")
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
    onRetry: () -> Unit,
    onSelect: (LevelUpAddress) -> Unit,
    onDelete: (LevelUpAddress) -> Unit
) {
    when (resource) {
        LevelUpResource.Loading -> LoadingView()
        is LevelUpResource.Error -> ErrorView(
            message = "No pudimos obtener tus direcciones",
            onRetry = onRetry
        )
        is LevelUpResource.Success -> {
            val addresses = resource.data
            if (addresses.isEmpty()) {
                EmptyAddressView()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(addresses, key = { it.id }) { address ->
                        AddressCard(
                            address = address,
                            onSelect = { onSelect(address) },
                            onDelete = { onDelete(address) }
                        )
                    }
                }
            }
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
