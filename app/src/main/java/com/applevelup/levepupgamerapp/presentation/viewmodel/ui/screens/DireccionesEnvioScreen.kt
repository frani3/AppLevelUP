package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

// --- DATOS DE EJEMPLO (Esto vendría de tu ViewModel) ---
data class Address(
    val id: Int,
    val alias: String, // "Casa", "Oficina", etc.
    val street: String,
    val city: String,
    val details: String,
    val isDefault: Boolean
)

val sampleAddresses = mutableStateListOf(
    Address(1, "Casa", "Av. Siempre Viva 742", "Springfield", "Portón Verde", true),
    Address(2, "Oficina", "Calle Falsa 123", "Shelbyville", "Piso 3, oficina B", false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DireccionesEnvioScreen(navController: NavController) {
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("agregar_direccion") },
                containerColor = PrimaryPurple,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Nueva Dirección")
            }
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        if (sampleAddresses.isEmpty()) {
            EmptyAddressView()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleAddresses, key = { it.id }) { address ->
                    AddressCard(
                        address = address,
                        onEdit = { /* TODO: Lógica para editar */ },
                        onDelete = {
                            sampleAddresses.remove(address)
                        }
                    )
                }
            }
        }
    }
}

// --- COMPONENTES DE LA PANTALLA DE DIRECCIONES ---

@Composable
fun AddressCard(
    address: Address,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val borderColor = if (address.isDefault) PrimaryPurple else CardBackgroundColor

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        modifier = Modifier.border(2.dp, borderColor, MaterialTheme.shapes.large)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = address.alias,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                if (address.isDefault) {
                    Text(
                        text = "Predeterminada",
                        color = PrimaryPurple,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(PrimaryPurple.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = address.street, color = Color.LightGray, fontSize = 16.sp)
            Text(text = address.city, color = Color.LightGray, fontSize = 16.sp)
            if (address.details.isNotBlank()) {
                Text(text = address.details, color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar Dirección", tint = Color.Gray)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Dirección", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun EmptyAddressView() {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "No hay direcciones",
                tint = Color.Gray,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("No tienes direcciones guardadas", fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("¡Agrega una dirección para tus próximos pedidos!", color = Color.Gray, modifier = Modifier.padding(horizontal = 32.dp))
        }
    }
}

