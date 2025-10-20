package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextAlign
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

// --- DATOS DE EJEMPLO (Esto vendría de tu ViewModel) ---
data class ProductDetail(
    val id: Int,
    val name: String,
    val price: Double,
    val oldPrice: Double?,
    val rating: Float,
    val reviewCount: Int,
    val imageRes: Int
)

// --- BASE DE DATOS DE PRODUCTOS COMPLETA ---
val allProducts = mapOf(
    "Juegos de Mesa" to listOf(
        ProductDetail(101, "Catan - El Juego", 49.99, 59.99, 4.8f, 1345, R.drawable.catan_product),
        ProductDetail(102, "Carcassonne - Edición Plus", 39.99, null, 4.7f, 987, R.drawable.carcassonne_product)
    ),
    "Accesorios" to listOf(
        ProductDetail(201, "Headset 7.1 Surround", 89.99, null, 4.6f, 854, R.drawable.audifonos_product)
    ),
    "Consolas" to listOf(
        ProductDetail(301, "PlayStation 5", 549.99, null, 4.9f, 3012, R.drawable.p5_product)
    ),
    "Computadores Gamers" to listOf(
        ProductDetail(401, "Notebook Gamer Asus", 1249.99, 1499.99, 4.8f, 451, R.drawable.pc_product)
    ),
    "Sillas Gamers" to listOf(
        ProductDetail(501, "Silla Gamer Ergonómica", 199.99, null, 4.7f, 1123, R.drawable.silla_product)
    ),
    "Mouse" to listOf(
        ProductDetail(601, "Mouse Logitech G502 HERO", 64.99, 79.99, 4.9f, 2054, R.drawable.mouse_product)
    ),
    "Mousepad" to listOf(
        ProductDetail(701, "Mousepad XXL", 29.99, null, 4.8f, 1500, R.drawable.mousepad_product)
    ),
    "Poleras Personalizadas" to emptyList(), // Categoría sin productos de ejemplo
    "Polerones Gamers Personalizados" to emptyList() // Categoría sin productos de ejemplo
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    categoryName: String // Recibimos el nombre de la categoría
) {
    // Obtenemos la lista de productos para esta categoría
    val products = allProducts.entries.firstOrNull {
        it.key.equals(categoryName, ignoreCase = true)
    }?.value ?: emptyList()



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(categoryName, fontWeight = FontWeight.Bold) }, // Título dinámico
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                    }
                }
            )
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        // Si no hay productos, muestra una vista especial
        if (products.isEmpty()) {
            EmptyProductView(categoryName)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products, key = { it.id }) { product ->
                    ProductListItem(
                        product = product,
                        onAddToCart = { /* TODO */ },
                        navController = navController
                    )
                }
            }
        }
    }
}

// Vista para cuando no hay productos
@Composable
fun EmptyProductView(categoryName: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("¡Uy!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("No hay productos en la categoría \"$categoryName\" por ahora.", color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}


// --- COMPONENTES (Sin cambios) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChips() {
    var selectedChip by remember { mutableStateOf("Relevancia") }
    val filters = listOf("Relevancia", "Precio", "Más vendidos")

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedChip == filter,
                onClick = { selectedChip = filter },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = CardBackgroundColor,
                    labelColor = Color.LightGray,
                    selectedContainerColor = PrimaryPurple,
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedChip == filter,
                    borderWidth = 0.dp,
                    selectedBorderWidth = 0.dp
                )
            )
        }
    }
}

@Composable
fun ProductListItem(
    product: ProductDetail,
    onAddToCart: () -> Unit,
    navController: NavController
) {
    Card(
        modifier = Modifier.clickable {
            navController.navigate("product_detail/${product.id}")
        },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 17.sp, maxLines = 2)
                Spacer(modifier = Modifier.height(6.dp))
                RatingBar(rating = product.rating, reviewCount = product.reviewCount)
                Spacer(modifier = Modifier.height(8.dp))
                Price(price = product.price, oldPrice = product.oldPrice)
            }
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = PrimaryPurple.copy(alpha = 0.1f),
                onClick = onAddToCart
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Agregar al Carrito",
                        tint = PrimaryPurple,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBar(rating: Float, reviewCount: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "$rating", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
        Text(text = " ($reviewCount)", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun Price(price: Double, oldPrice: Double?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$${"%.3f".format(price)}", fontWeight = FontWeight.Bold, color = PrimaryPurple, fontSize = 18.sp)
        if (oldPrice != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$${"%.3f".format(oldPrice)}",
                color = Color.Gray,
                fontSize = 14.sp,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}

