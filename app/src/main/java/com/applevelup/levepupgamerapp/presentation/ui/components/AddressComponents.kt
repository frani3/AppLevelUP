package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.presentation.ui.theme.CardBackgroundColor
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple

// 🔹 Tarjeta individual de dirección
@Composable
fun AddressCard(
    address: LevelUpAddress,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit = {},
    onSetPrimary: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, PrimaryPurple, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = PrimaryPurple,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = address.fullName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Row {
                    // Botón para marcar como principal
                    IconButton(onClick = onSetPrimary) {
                        Icon(
                            imageVector = if (address.isPrimary) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = if (address.isPrimary) "Dirección principal" else "Marcar como principal",
                            tint = if (address.isPrimary) PrimaryPurple else Color.Gray
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar dirección",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar dirección",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Línea de dirección
            Text(address.line1, color = Color.LightGray, fontSize = 14.sp)
            // Ciudad y región
            val secondary = listOfNotNull(
                address.city.takeIf { it.isNotBlank() },
                address.region.takeIf { it.isNotBlank() }
            ).joinToString(separator = " · ")
            if (secondary.isNotBlank()) {
                Text(secondary, color = Color.Gray, fontSize = 13.sp)
            }
            // País
            if (address.country.isNotBlank()) {
                Text(address.country, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

// 🔹 Vista cuando no hay direcciones guardadas
@Composable
fun EmptyAddressView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Sin direcciones",
                tint = PrimaryPurple,
                modifier = Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No tienes direcciones guardadas",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Agrega una dirección para tus futuros envíos.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}
