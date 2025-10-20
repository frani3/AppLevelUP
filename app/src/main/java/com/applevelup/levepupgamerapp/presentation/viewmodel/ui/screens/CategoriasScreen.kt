package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

// --- DATOS DE EJEMPLO (Esto vendría de tu ViewModel) ---
data class CategoryInfo(
    val name: String,
    val icon: ImageVector,
    val sampleProducts: List<String>
)

val allCategories = listOf(
    CategoryInfo("Juegos de Mesa", Icons.Default.Casino, listOf("Catan", "Carcassonne")),
    CategoryInfo("Accesorios", Icons.Default.Headset, listOf("Control Xbox Series X", "Auriculares HyperX Cloud II")),
    CategoryInfo("Consolas", Icons.Default.VideogameAsset, listOf("PlayStation 5", "Nintendo Switch OLED")),
    CategoryInfo("Computadores Gamers", Icons.Default.DesktopWindows, listOf("PC Gamer ASUS ROG Strix", "Notebook MSI Katana")),
    CategoryInfo("Sillas Gamers", Icons.Default.Chair, listOf("Silla Gamer Secretlab Titan", "Silla Gamer Cougar Armor One")),
    CategoryInfo("Mouse", Icons.Default.Mouse, listOf("Mouse Logitech G502 HERO", "Mouse Razer DeathAdder V2")),
    CategoryInfo("Mousepad", Icons.Default.SquareFoot, listOf("Mousepad Razer Goliathus", "Mousepad Logitech G Powerplay")),
    CategoryInfo("Poleras Personalizadas", Icons.Default.Checkroom, listOf("Polera Gamer Personalizada", "Polera Retro Arcade")),
    CategoryInfo("Polerones Gamers", Icons.Default.Checkroom, listOf("Polerón Gamer 'Respawn'", "Polerón Level-Up Logo"))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Categorías", fontWeight = FontWeight.Bold) },
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Dos columnas en la cuadrícula
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(allCategories) { category ->
                CategoryCard(category = category, onClick = {
                    // TODO: Navegar a la página de productos de esta categoría
                    navController.navigate("productos/${category.name.lowercase().replaceFirstChar { it.uppercase() }.replace("_", " ")}")

                })
            }
        }
    }
}


// --- COMPONENTE DE TARJETA DE CATEGORÍA ---
@Composable
fun CategoryCard(
    category: CategoryInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .aspectRatio(1f) // Hace que la tarjeta sea cuadrada
            .border(2.dp, PrimaryPurple, MaterialTheme.shapes.large),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Fila superior con título e ícono
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = category.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f)) // Espacio flexible para empujar productos hacia abajo
            // Lista de productos de ejemplo
            Column {
                category.sampleProducts.forEach { productName ->
                    Text(
                        text = "• $productName",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}