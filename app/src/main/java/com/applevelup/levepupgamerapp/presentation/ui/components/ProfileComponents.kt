package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.theme.CardBackgroundColor
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple

// 🔹 Encabezado del perfil (foto + nombre + correo)
@Composable
fun ProfileHeader(user: UserProfile) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = user.avatarRes),
            contentDescription = "Avatar de usuario",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(3.dp, PrimaryPurple, CircleShape)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(user.name, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
        Text(user.email, color = Color.Gray, fontSize = 14.sp)
    }
}

// 🔹 Panel de estadísticas (pedidos, wishlist, cupones)
@Composable
fun StatsPanel(user: UserProfile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatCard("Pedidos", user.orderCount)
        StatCard("Wishlist", user.wishlistCount)
        StatCard("Cupones", user.couponCount)
    }
}

@Composable
fun StatCard(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$value", color = PrimaryPurple, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(label, color = Color.Gray, fontSize = 14.sp)
    }
}

// 🔹 Historial de pedidos
@Composable
fun OrderHistory(orders: List<Order>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Historial de pedidos",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (orders.isEmpty()) {
            Text("No hay pedidos recientes", color = Color.Gray)
        } else {
            orders.forEach { order -> OrderCard(order) }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(order.id, color = Color.White, fontWeight = FontWeight.Bold)
                Text(order.date, color = Color.Gray, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(order.status, color = PrimaryPurple, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(order.total, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 🔹 Menú de ajustes
@Composable
fun SettingsMenu(
    navController: NavController,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Configuración",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SettingItem("Editar Perfil", Icons.Default.Edit) {
            navController.navigate(Destinations.EditProfile.route)
        }

        SettingItem("Direcciones de Envío", Icons.Default.LocationOn) {
            navController.navigate("direcciones_envio")
        }

        SettingItem("Métodos de Pago", Icons.Default.Payment) {
            navController.navigate("metodos_pago")
        }

        SettingItem("Notificaciones", Icons.Default.Notifications) {
            navController.navigate("notificaciones")
        }

    SettingItem("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp, onLogout)
    }
}

// 🔹 Ítem individual del menú
@Composable
fun SettingItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = PrimaryPurple, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, color = Color.White, fontSize = 16.sp)
    }
}
