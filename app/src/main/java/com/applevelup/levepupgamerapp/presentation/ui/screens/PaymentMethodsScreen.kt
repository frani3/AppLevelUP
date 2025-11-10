package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.components.EmptyPaymentView
import com.applevelup.levepupgamerapp.presentation.ui.components.PaymentMethodCard
import com.applevelup.levepupgamerapp.presentation.ui.theme.*
import com.applevelup.levepupgamerapp.presentation.viewmodel.PaymentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsScreen(
    navController: NavController,
    viewModel: PaymentViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadMethods()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Métodos de Pago", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Destinations.AddPaymentMethod.route) },
                containerColor = PrimaryPurple,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Nuevo Método de Pago")
            }
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple)
            }
        } else if (state.methods.isEmpty()) {
            EmptyPaymentView()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.methods, key = { it.id }) { method ->
                    PaymentMethodCard(
                        method = method,
                        onSelect = {
                            viewModel.markAsDefault(method.id)
                            navController.popBackStack()
                        },
                        onDelete = { viewModel.deleteMethod(method.id) }
                    )
                }
            }
        }
    }
}
