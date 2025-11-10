package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.components.PaymentForm
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.viewmodel.AddPaymentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentMethodScreen(
    navController: NavController,
    viewModel: AddPaymentViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar Tarjeta", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                PaymentForm(
                    state = state,
                    onNameChange = viewModel::onNameChange,
                    onNumberChange = viewModel::onNumberChange,
                    onExpiryChange = viewModel::onExpiryChange,
                    onCvvChange = viewModel::onCvvChange
                )
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (viewModel.saveCard()) {
                                navController.popBackStack()
                            }
                        }
                    },
                    enabled = state.isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("Guardar Tarjeta", color = Color.White, fontSize = 18.sp)
                }

                if (state.showError && !state.isValid) {
                    Text(
                        "Por favor completa todos los campos correctamente.",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}
