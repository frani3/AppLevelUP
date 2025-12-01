package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import java.io.File
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.UserProfile
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.theme.CardBackgroundColor
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple

// 🔹 Encabezado del perfil (foto + nombre + correo)
@Composable
fun ProfileHeader(user: UserProfile, onChangePhotoRequest: () -> Unit) {
    val context = LocalContext.current
    val imageModel = remember(user.photoUri) {
        when {
            user.photoUri.isNullOrBlank() -> null
            user.photoUri.startsWith("content://") || user.photoUri.startsWith("file://") -> user.photoUri
            else -> File(user.photoUri)
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .border(3.dp, PrimaryPurple, CircleShape)
                    .clickable(onClick = onChangePhotoRequest)
            ) {
                if (imageModel != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageModel)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Avatar de usuario",
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = user.avatarRes),
                        contentDescription = "Avatar de usuario",
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            IconButton(
                onClick = onChangePhotoRequest,
                modifier = Modifier
                    .size(40.dp)
                    .offset((-6).dp, (-6).dp)
                    .clip(CircleShape)
                    .background(PrimaryPurple)
                    .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Cambiar foto",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(user.name, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
        Text(user.email, color = Color.Gray, fontSize = 14.sp)
        if (!user.address.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(user.address, color = Color.LightGray, fontSize = 13.sp)
        }
        if (user.hasLifetimeDiscount) {
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                color = PrimaryPurple.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "20% OFF vitalicio activado",
                    color = PrimaryPurple,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
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
    // Formatear ID corto (últimos 8 caracteres)
    val shortId = if (order.id.length > 8) "...${order.id.takeLast(8)}" else order.id
    
    // Formatear fecha (de ISO a formato legible)
    val formattedDate = try {
        val isoDate = order.date.replace("T", " ").take(16) // "2025-12-01 01:25"
        isoDate
    } catch (e: Exception) {
        order.date
    }
    
    // Traducir status
    val displayStatus = when (order.status.lowercase()) {
        "pending" -> "Pendiente"
        "paid", "pagado" -> "Pagado"
        "shipped" -> "Enviado"
        "delivered" -> "Entregado"
        "cancelled" -> "Cancelado"
        else -> order.status
    }
    
    // Color del status
    val statusColor = when (order.status.lowercase()) {
        "paid", "pagado" -> Color(0xFF4CAF50) // Verde
        "pending" -> Color(0xFFFFC107) // Amarillo
        "shipped" -> Color(0xFF2196F3) // Azul
        "delivered" -> Color(0xFF4CAF50) // Verde
        "cancelled" -> Color(0xFFF44336) // Rojo
        else -> PrimaryPurple
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fila superior: ID y Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${shortId}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = statusColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = displayStatus,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Fila inferior: Fecha, Items y Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = formattedDate,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${order.itemCount} producto${if (order.itemCount != 1) "s" else ""}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = order.total,
                    color = PrimaryPurple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
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
            navController.navigate(Destinations.NotificationSettings.route)
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
