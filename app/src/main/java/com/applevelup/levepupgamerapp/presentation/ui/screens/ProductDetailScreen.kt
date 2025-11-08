package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int
) {
    val product = allProducts.values.flatten().find { it.id == productId }
    var quantity by remember { mutableStateOf(1) }
    val totalPrice = product?.price?.times(quantity) ?: 0.0

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = TopBarAndDrawerColor.copy(alpha = 1f)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Total", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        "$${"%.3f".format(totalPrice)}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Button(
                    onClick = { /* TODO: Lógica para agregar al carrito */ },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar al Carrito", fontSize = 16.sp)
                }
            }
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        if (product == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Producto no encontrado", color = Color.White)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                ) {
                    item { ProductImageCarousel(product = product) }
                    item {
                        Column(Modifier.padding(16.dp)) {
                            ProductHeader(product = product)
                            Spacer(Modifier.height(16.dp))
                            DetailPrice(price = product.price, oldPrice = product.oldPrice)
                            Spacer(Modifier.height(16.dp))
                            Text("En Stock", color = Color.Green, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Cantidad", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            DetailQuantityControl(
                                quantity = quantity,
                                onIncrease = { quantity++ },
                                onDecrease = { if (quantity > 1) quantity-- }
                            )
                        }
                    }
                    item { Divider(Modifier.padding(16.dp), color = Color.Gray.copy(alpha = 0.3f)) }
                    item { ExpandableDescription() }
                    item { Divider(Modifier.padding(16.dp), color = Color.Gray.copy(alpha = 0.3f)) }
                    item { RelatedProductsSection(navController = navController) }
                    item { Spacer(Modifier.height(16.dp)) }
                }

                TransparentTopBar(navController = navController)
            }
        }
    }
}

// --- COMPONENTES ---

@Composable
fun TransparentTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FloatingIconButton(icon = Icons.AutoMirrored.Filled.ArrowBack, onClick = { navController.popBackStack() })
        FloatingIconButton(icon = Icons.Default.Share, onClick = { /* TODO */ })
    }
}

@Composable
fun FloatingIconButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}

@Composable
fun ProductHeader(product: ProductDetail) {
    var isFavorite by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Juegos de Mesa", color = PrimaryPurple, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color.White, lineHeight = 32.sp)
            Spacer(Modifier.height(8.dp))
            DetailRatingBar(rating = product.rating, reviewCount = product.reviewCount)
        }
        IconButton(onClick = { isFavorite = !isFavorite }) {
            Icon(
                if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Añadir a favoritos",
                tint = if (isFavorite) PrimaryPurple else Color.Gray,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun ExpandableDescription() {
    var expanded by remember { mutableStateOf(false) }
    val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi. Proin porttitor, orci nec nonummy molestie, enim est eleifend mi, non fermentum diam nisl sit amet erat."

    Column(Modifier.padding(horizontal = 16.dp)) {
        Text("Descripción", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.animateContentSize()) {
            Text(
                text = description,
                color = Color.LightGray,
                lineHeight = 22.sp,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = if (expanded) "Leer menos" else "Leer más",
            color = PrimaryPurple,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(top = 4.dp)
        )
    }
}

@Composable
fun RelatedProductsSection(navController: NavController) {
    val relatedProducts = allProducts["Juegos de Mesa"] ?: emptyList()

    Column {
        Text(
            "Productos Similares",
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
            items(relatedProducts) { product ->
                ProductCard(
                    product = Product(product.name, "$${"%.3f".format(product.price)}", product.imageRes),
                    onClick = { navController.navigate("product_detail/${product.id}") }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImageCarousel(product: ProductDetail) {
    val images = listOf(product.imageRes, product.imageRes, product.imageRes)
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(contentAlignment = Alignment.BottomCenter) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Imagen del producto ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) PrimaryPurple else Color.LightGray.copy(alpha = 0.5f)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

// --- FUNCIONES RENOMBRADAS PARA EVITAR CONFLICTOS ---

@Composable
fun DetailRatingBar(rating: Float, reviewCount: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "$rating", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
        Text(text = " ($reviewCount reseñas)", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun DetailPrice(price: Double, oldPrice: Double?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$${"%.3f".format(price)}", fontWeight = FontWeight.Bold, color = PrimaryPurple, fontSize = 24.sp)
        if (oldPrice != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$${"%.3f".format(oldPrice)}", color = Color.Gray, fontSize = 16.sp, textDecoration = TextDecoration.LineThrough)
        }
    }
}

@Composable
fun DetailQuantityControl(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(35.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(17.5.dp))
    ) {
        IconButton(onClick = onDecrease, modifier = Modifier.size(35.dp)) {
            Icon(Icons.Default.Remove, "Disminuir", tint = Color.White)
        }
        Text(text = quantity.toString(), color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp))
        IconButton(onClick = onIncrease, modifier = Modifier.size(35.dp)) {
            Icon(Icons.Default.Add, "Aumentar", tint = Color.White)
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)) {
                Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(product.price, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryPurple)
            }
        }
    }
}
