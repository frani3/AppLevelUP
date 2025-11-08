package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

// --- DATOS DE EJEMPLO (Esto vendría de tu ViewModel) ---
enum class CardType {
    VISA, MASTERCARD, OTHER
}

data class PaymentMethod(
    val id: Int,
    val cardType: CardType,
    val lastFourDigits: String,
    val expiryDate: String,
    val isDefault: Boolean
)

val samplePaymentMethods = mutableStateListOf(
    PaymentMethod(1, CardType.VISA, "4242", "12/27", true),
    PaymentMethod(2, CardType.MASTERCARD, "5578", "08/26", false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetodosPagoScreen(navController: NavController) {
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
                onClick = { navController.navigate("agregar_metodo_pago") }, // <-- ASÍ
                containerColor = PrimaryPurple,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Nuevo Método de Pago")
            }
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        if (samplePaymentMethods.isEmpty()) {
            EmptyPaymentView()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(samplePaymentMethods, key = { it.id }) { method ->
                    PaymentMethodCard(
                        method = method,
                        onDelete = {
                            samplePaymentMethods.remove(method)
                        }
                    )
                }
            }
        }
    }
}

// --- COMPONENTES DE LA PANTALLA DE MÉTODOS DE PAGO ---

@Composable
fun PaymentMethodCard(
    method: PaymentMethod,
    onDelete: () -> Unit
) {
    val borderColor = if (method.isDefault) PrimaryPurple else CardBackgroundColor

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        modifier = Modifier.border(2.dp, borderColor, MaterialTheme.shapes.large)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de la tarjeta
            val cardIcon = when (method.cardType) {
                CardType.VISA -> R.drawable.ic_visa
                CardType.MASTERCARD -> R.drawable.ic_mastercard
                else -> null // No mostramos ícono si no es una marca conocida
            }
            if (cardIcon != null) {
                Image(
                    painter = painterResource(id = cardIcon),
                    contentDescription = method.cardType.name,
                    modifier = Modifier.height(30.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = "Tarjeta de crédito",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información de la tarjeta
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "**** **** **** ${method.lastFourDigits}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Expira: ${method.expiryDate}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            // Etiqueta de predeterminada y botón de eliminar
            Column(horizontalAlignment = Alignment.End) {
                if (method.isDefault) {
                    Text(
                        text = "Predeterminada",
                        color = PrimaryPurple,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun EmptyPaymentView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = "No hay métodos de pago",
                tint = Color.Gray,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("No tienes métodos de pago", fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Agrega una tarjeta para compras más rápidas.",
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}


