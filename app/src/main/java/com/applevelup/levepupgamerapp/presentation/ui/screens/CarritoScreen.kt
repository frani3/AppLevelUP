package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

// --- DATOS DE EJEMPLO (Esto vendría de tu CarritoViewModel) ---
data class CartItem(
    val id: Int,
    val name: String,
    val price: Double,
    val imageRes: Int,
    var quantity: Int
)

// Usamos mutableStateListOf para que la UI se actualice al borrar items
val sampleCartItems = mutableStateListOf(
    CartItem(1, "Teclado Mecánico RGB", 99.99, R.drawable.teclado_product, 1),
    CartItem(2, "Mouse Gamer Inalámbrico", 64.99, R.drawable.mouse_product, 2)
)


@OptIn(ExperimentalMaterial3Api::class) // <-- ESTA ES LA LÍNEA CORREGIDA
@Composable
fun CarritoScreen(navController: NavController) {
    // Estado para forzar la recomposición cuando cambiamos la cantidad
    var refreshKey by remember { mutableStateOf(0) }

    val subtotal = sampleCartItems.sumOf { it.price * it.quantity }
    val shippingCost = if (subtotal > 0) 5.99 else 0.0
    val total = subtotal + shippingCost

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
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
        if (sampleCartItems.isEmpty()) {
            EmptyCartView(navController)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Lista de productos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        // Dejamos espacio abajo para el resumen
                        .padding(bottom = 170.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = sampleCartItems,
                        key = { it.id } // Key para animaciones al borrar
                    ) { item ->
                        CartItemCard(
                            item = item,
                            onQuantityChange = {
                                // Forzamos la actualización del estado
                                refreshKey++
                            },
                            onDelete = {
                                sampleCartItems.remove(item)
                            }
                        )
                    }
                }

                // Resumen y botón de pago (fijo abajo)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(TopBarAndDrawerColor)
                        .padding(16.dp)
                ) {
                    SummarySection(subtotal, shippingCost, total, refreshKey)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO: Navegar a la pantalla de pago */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text(text = "Proceder al Pago", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

// --- COMPONENTES DE LA PANTALLA DE CARRITO ---

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "$${"%.2f".format(item.price)}", color = PrimaryPurple, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(8.dp))
                QuantityControl(
                    quantity = item.quantity,
                    onIncrease = {
                        item.quantity++
                        onQuantityChange()
                    },
                    onDecrease = {
                        if (item.quantity > 1) {
                            item.quantity--
                            onQuantityChange()
                        }
                    }
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar item",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun QuantityControl(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(30.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
    ) {
        IconButton(onClick = onDecrease, modifier = Modifier.size(30.dp)) {
            Icon(Icons.Default.Remove, "Disminuir", tint = Color.White)
        }
        Text(
            text = quantity.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = onIncrease, modifier = Modifier.size(30.dp)) {
            Icon(Icons.Default.Add, "Aumentar", tint = Color.White)
        }
    }
}

@Composable
fun SummarySection(subtotal: Double, shippingCost: Double, total: Double, key: Int) {
    // El 'key' fuerza la recomposición de este Composable
    // cuando la cantidad de un producto cambia.
    key.let {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryRow("Subtotal", "$${"%.2f".format(subtotal)}")
            SummaryRow("Envío", "$${"%.2f".format(shippingCost)}")
            Divider(color = Color.Gray.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 4.dp))
            SummaryRow("Total", "$${"%.2f".format(total)}", isTotal = true)
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    val fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
    val fontSize = if (isTotal) 20.sp else 16.sp
    val color = if (isTotal) Color.White else Color.LightGray

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = color, fontSize = fontSize, fontWeight = fontWeight)
        Text(text = value, color = color, fontSize = fontSize, fontWeight = fontWeight)
    }
}

@Composable
fun EmptyCartView(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.ShoppingCartCheckout,
                contentDescription = "Carrito Vacío",
                tint = Color.Gray,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Tu carrito está vacío", fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("¡Encuentra los mejores artículos gamer!", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    navController.navigate("landing_page") {
                        // Limpia el historial hasta la landing page
                        popUpTo(navController.graph.startDestinationId)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Text("Explorar productos")
            }
        }
    }
}

