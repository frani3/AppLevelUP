@file:OptIn(ExperimentalMaterial3Api::class)

package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpRegion
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
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
            snackbarHostState.showSnackbar("Nombre, dirección, ciudad y región son obligatorios")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar nueva dirección", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Loading indicator for regions
            if (state.isLoadingRegions) {
                CircularProgressIndicator(
                    color = PrimaryPurple,
                    modifier = Modifier.padding(16.dp)
                )
                Text("Cargando regiones...", color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
            }

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
                                // Navegar a la lista de direcciones limpiando el back stack
                                navController.navigate(Destinations.Addresses.route) {
                                    popUpTo(Destinations.Addresses.route) { inclusive = true }
                                }
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
                    Text("GUARDAR DIRECCIÓN", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (state.showValidationErrors && !state.isValid) {
                Text(
                    "Completa todos los campos requeridos",
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
    val cardBackground = Color(0xFF1A1A2E)
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        // Nombre del destinatario
        FormLabel("NOMBRE DEL DESTINATARIO")
        OutlinedTextField(
            value = state.fullName,
            onValueChange = viewModel::onFullNameChange,
            placeholder = { Text("Ej. Alex Gamer", color = Color.Gray) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = addressTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        // Dirección
        FormLabel("DIRECCIÓN")
        OutlinedTextField(
            value = state.line1,
            onValueChange = viewModel::onLine1Change,
            placeholder = { Text("Ej. Av. Gamer 1337", color = Color.Gray) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = addressTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        // Región dropdown
        FormLabel("REGIÓN")
        DropdownSelector(
            selectedText = state.selectedRegion?.name ?: "Selecciona una región",
            isExpanded = state.isRegionDropdownExpanded,
            onToggle = viewModel::toggleRegionDropdown,
            onDismiss = viewModel::dismissRegionDropdown,
            placeholder = state.selectedRegion == null
        ) {
            state.regions.forEach { region ->
                DropdownMenuItem(
                    text = { Text(region.name, color = Color.White) },
                    onClick = { viewModel.onRegionSelected(region) },
                    trailingIcon = if (state.selectedRegion?.id == region.id) {
                        { Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryPurple) }
                    } else null
                )
            }
        }

        // Ciudad/Comuna dropdown (depends on region)
        FormLabel("CIUDAD / COMUNA")
        DropdownSelector(
            selectedText = if (state.selectedComuna.isNotBlank()) state.selectedComuna 
                          else if (state.selectedRegion == null) "Primero elige una región"
                          else "Selecciona una comuna",
            isExpanded = state.isComunaDropdownExpanded,
            onToggle = viewModel::toggleComunaDropdown,
            onDismiss = viewModel::dismissComunaDropdown,
            enabled = state.selectedRegion != null,
            placeholder = state.selectedComuna.isBlank()
        ) {
            state.comunas.forEach { comuna ->
                DropdownMenuItem(
                    text = { Text(comuna, color = Color.White) },
                    onClick = { viewModel.onComunaSelected(comuna) },
                    trailingIcon = if (state.selectedComuna == comuna) {
                        { Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryPurple) }
                    } else null
                )
            }
        }

        // País (fixed value)
        FormLabel("PAÍS")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(cardBackground)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text("Chile", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox para dirección principal
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.setAsDefault,
                onCheckedChange = viewModel::onDefaultChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = PrimaryPurple,
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Marcar como dirección principal",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        color = Color.Gray,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun DropdownSelector(
    selectedText: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onDismiss: () -> Unit,
    enabled: Boolean = true,
    placeholder: Boolean = false,
    content: @Composable () -> Unit
) {
    val cardBackground = Color(0xFF1A1A2E)
    
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(cardBackground)
                .border(
                    width = 1.dp,
                    color = if (isExpanded) PrimaryPurple else Color.Gray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(enabled = enabled) { onToggle() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedText,
                    color = if (placeholder) Color.Gray else Color.White
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = if (enabled) Color.White else Color.Gray
                )
            }
        }
        
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 300.dp)
                .background(cardBackground)
        ) {
            content()
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
