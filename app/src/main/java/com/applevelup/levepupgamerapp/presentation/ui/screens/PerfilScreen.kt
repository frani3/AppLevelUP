package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

// --- DATOS DE EJEMPLO (Esto vendría de tu ViewModel) ---
data class UserProfile(
    val name: String,
    val email: String,
    val avatarRes: Int,
    val orderCount: Int,
    val wishlistCount: Int,
    val couponCount: Int
)

data class Order(
    val id: String,
    val date: String,
    val status: String,
    val total: String,
    val itemCount: Int
)

val sampleUserProfile = UserProfile(
    name = "FranI3",
    email = "frani3@email.com",
    avatarRes = R.drawable.avatar_placeholder,
    orderCount = 12,
    wishlistCount = 8,
    couponCount = 3
)

val sampleOrders = listOf(
    Order("#345-1", "15 Oct 2025", "Entregado", "$149.990", 2),
    Order("#312-8", "02 Oct 2025", "Entregado", "$89.990", 1),
    Order("#299-3", "21 Sep 2025", "Cancelado", "$29.990", 1)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
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
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { ProfileHeader(user = sampleUserProfile) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { StatsPanel(user = sampleUserProfile) }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            item { OrderHistory(orders = sampleOrders) }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            item { SettingsMenu(navController = navController) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}


// --- COMPONENTES DE LA PANTALLA DE PERFIL ---

@Composable
fun ProfileHeader(user: UserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val purpleGradient = Brush.verticalGradient(
            colors = listOf(PrimaryPurple, Color(0xFF6A008A))
        )
        Image(
            painter = painterResource(id = user.avatarRes),
            contentDescription = "Avatar de usuario",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    width = 4.dp,
                    brush = purpleGradient,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = user.name,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White
        )
        Text(
            text = user.email,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun StatsPanel(user: UserProfile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatCard("Pedidos", user.orderCount.toString(), Icons.AutoMirrored.Filled.ReceiptLong)
        StatCard("Favoritos", user.wishlistCount.toString(), Icons.Default.Favorite)
        StatCard("Cupones", user.couponCount.toString(), Icons.Default.ConfirmationNumber)
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = PrimaryPurple)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
            Text(text = title, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun OrderHistory(orders: List<Order>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Historial de Pedidos",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(orders) { order ->
                OrderCard(order = order)
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { /* Navegar a detalles del pedido */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pedido ${order.id}",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = order.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray.copy(alpha = 0.3f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "${order.itemCount} productos", fontSize = 14.sp, color = Color.LightGray)
                    Text(text = order.total, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryPurple)
                }
                Text(
                    text = order.status,
                    color = if (order.status == "Entregado") Color.Green else if (order.status == "Cancelado") Color.Red else Color.Yellow,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(
                            (if (order.status == "Entregado") Color.Green else if (order.status == "Cancelado") Color.Red else Color.Yellow).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsMenu(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SettingItem(
            title = "Gestionar Cuenta",
            icon = Icons.Default.Person,
            onClick = { navController.navigate("gestionar_cuenta") }
        )
        SettingItem(
            title = "Direcciones de Envío",
            icon = Icons.Default.LocalShipping,
            onClick = { navController.navigate("direcciones_envio") }
        )
        SettingItem(
            title = "Métodos de Pago",
            icon = Icons.Default.Payment,
            onClick = { navController.navigate("metodos_pago") }
        )
        SettingItem(
            title = "Notificaciones",
            icon = Icons.Default.Notifications,
            onClick = { navController.navigate("notificaciones") }
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray.copy(alpha = 0.3f))
        SettingItem(
            title = "Cerrar Sesión",
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            isLogout = true,
            onClick = {
                // Navegar a Login y limpiar todo el historial
                navController.navigate("login") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Composable
fun SettingItem(title: String, icon: ImageVector, isLogout: Boolean = false, onClick: () -> Unit) {
    val contentColor = if (isLogout) Color.Red else Color.White
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = contentColor,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        if (!isLogout) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}