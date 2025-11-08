package com.applevelup.levepupgamerapp.presentation.ui.screens

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
fun AgregarEditarDireccionScreen(navController: NavController) {

    // --- ESTADOS (Esto vendría de tu ViewModel) ---
    var alias by rememberSaveable { mutableStateOf("") }
    var street by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var region by rememberSaveable { mutableStateOf("") }
    var postalCode by rememberSaveable { mutableStateOf("") }
    var details by rememberSaveable { mutableStateOf("") }
    var isDefault by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva Dirección", fontWeight = FontWeight.Bold) },
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
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = alias,
                    onValueChange = { alias = it }, // <-- ERROR CORREGIDO AQUÍ
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Alias (ej. Casa, Oficina)") },
                    colors = getTextFieldColors(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = street,
                    onValueChange = { street = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Calle y Número") },
                    colors = getTextFieldColors(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ciudad") },
                    colors = getTextFieldColors(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = region,
                    onValueChange = { region = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Región / Estado") },
                    colors = getTextFieldColors(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Código Postal") },
                    colors = getTextFieldColors(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Detalles adicionales (opcional)") },
                    colors = getTextFieldColors(),
                    singleLine = false,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Usar como dirección predeterminada", color = Color.White)
                    Switch(
                        checked = isDefault,
                        onCheckedChange = { isDefault = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PrimaryPurple,
                            checkedTrackColor = PrimaryPurple.copy(alpha = 0.5f)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        // TODO: Lógica para guardar la nueva dirección
                        // viewModel.addAddress(...)
                        navController.popBackStack() // Volvemos a la lista de direcciones
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text(text = "Guardar Dirección", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

// --- FUNCIÓN DE AYUDA AGREGADA ---
@Composable
private fun getTextFieldColors(): TextFieldColors {
    val textFieldBackgroundColor = Color.DarkGray.copy(alpha = 0.3f)
    val lightTextColor = Color.LightGray
    return TextFieldDefaults.colors(
        focusedContainerColor = textFieldBackgroundColor,
        unfocusedContainerColor = textFieldBackgroundColor,
        disabledContainerColor = textFieldBackgroundColor,
        focusedIndicatorColor = PrimaryPurple,
        unfocusedIndicatorColor = Color.Gray,
        focusedLabelColor = lightTextColor,
        unfocusedLabelColor = lightTextColor,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedTrailingIconColor = lightTextColor,
        unfocusedTrailingIconColor = lightTextColor
    )
}

