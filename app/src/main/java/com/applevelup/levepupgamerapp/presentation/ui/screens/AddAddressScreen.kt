@file:OptIn(ExperimentalMaterial3Api::class)

package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.viewmodel.AddAddressUiState
import com.applevelup.levepupgamerapp.presentation.viewmodel.AddAddressViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.levelup.LevelUpAddressViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.levelup.LevelUpAuthViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.levelup.LevelUpViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AddAddressScreen(
    navController: NavController,
    viewModel: AddAddressViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val factory = remember { LevelUpViewModelFactory() }
    val addressViewModel: LevelUpAddressViewModel = viewModel(factory = factory)
    val authViewModel: LevelUpAuthViewModel = viewModel(factory = factory)
    val profileState by authViewModel.profileState.collectAsState()
    val run = (profileState as? LevelUpResource.Success)?.data?.run

    LaunchedEffect(state.showValidationErrors, state.isValid) {
        if (state.showValidationErrors && !state.isValid) {
            snackbarHostState.showSnackbar("Alias, dirección, comuna y región son obligatorios")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva dirección", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryPurple,
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddressForm(state = state, viewModel = viewModel)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val currentRun = run
                    val input = viewModel.buildAddressInput()
                    if (currentRun == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Aún estamos cargando tu perfil. Inténtalo en unos segundos.")
                        }
                        return@Button
                    }
                    if (input == null) return@Button
                    coroutineScope.launch {
                        viewModel.setSaving(true)
                        when (val result = addressViewModel.createAddress(currentRun, input)) {
                            is LevelUpResult.Success -> {
                                snackbarHostState.showSnackbar("Dirección guardada")
                                addressViewModel.refreshAddresses(currentRun)
                                viewModel.resetForm()
                                navController.popBackStack()
                            }
                            is LevelUpResult.Failure -> {
                                snackbarHostState.showSnackbar(result.throwable.message ?: "No se pudo guardar la dirección")
                            }
                        }
                        viewModel.setSaving(false)
                    }
                },
                enabled = state.isValid && !state.isSaving && run != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text("Guardar dirección", color = Color.White, fontSize = 18.sp)
                }
            }

            if (state.showValidationErrors && !state.isValid) {
                Text(
                    "Alias, dirección, comuna y región son obligatorios",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            if (run == null) {
                Text(
                    text = "Esperando la información de tu perfil...",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun AddressForm(state: AddAddressUiState, viewModel: AddAddressViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.alias,
            onValueChange = viewModel::onAliasChange,
            label = { Text("Alias (ej. Casa, Oficina)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = addressTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.street,
            onValueChange = viewModel::onStreetChange,
            label = { Text("Dirección") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = addressTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.number,
            onValueChange = viewModel::onNumberChange,
            label = { Text("Número (opcional)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = addressTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.comuna,
            onValueChange = viewModel::onComunaChange,
            label = { Text("Comuna") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = addressTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.region,
            onValueChange = viewModel::onRegionChange,
            label = { Text("Región") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = addressTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.details,
            onValueChange = viewModel::onDetailsChange,
            label = { Text("Detalles adicionales (opcional)") },
            minLines = 2,
            maxLines = 3,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = addressTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        DefaultToggle(state = state, viewModel = viewModel)
    }
}

@Composable
private fun DefaultToggle(state: AddAddressUiState, viewModel: AddAddressViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("¿Usar como dirección predeterminada?", color = Color.White, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Se seleccionará automáticamente en tus próximas compras.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
            Switch(
                checked = state.setAsDefault,
                onCheckedChange = viewModel::onDefaultChange,
                colors = SwitchDefaults.colors(checkedThumbColor = PrimaryPurple)
            )
        }
        if (!state.setAsDefault) {
            TextButton(onClick = { viewModel.onDefaultChange(true) }) {
                Text("Marcar como predeterminada", color = PrimaryPurple)
            }
        }
    }
}

@Composable
private fun addressTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    focusedIndicatorColor = PrimaryPurple,
    unfocusedIndicatorColor = Color.Gray,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.LightGray,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = PrimaryPurple
)
