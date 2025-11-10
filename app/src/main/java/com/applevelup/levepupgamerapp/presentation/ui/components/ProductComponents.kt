package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.presentation.ui.theme.CardBackgroundColor
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.utils.PriceUtils

@Composable
fun EmptyProductView(categoryName: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("¡Uy!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(
                "No hay productos en la categoría \"$categoryName\" por ahora.",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProductListItem(
    product: Product,
    onAddToCart: () -> Unit,
    navController: NavController,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null
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
            ProductImage(
                imageRes = product.imageRes,
                imageUrl = product.imageUrl,
                imageUri = product.imageUri,
                contentDescription = product.name,
                modifier = Modifier
                    .size(110.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 17.sp, maxLines = 2)
                Spacer(modifier = Modifier.height(6.dp))
                RatingBar(rating = product.rating, reviewCount = product.reviews)
                Spacer(modifier = Modifier.height(8.dp))
                Price(price = product.price, oldPrice = product.oldPrice)
            }

            Column(
                modifier = Modifier.height(110.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (onToggleFavorite != null) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = PrimaryPurple.copy(alpha = 0.1f),
                        onClick = onToggleFavorite
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                                tint = if (isFavorite) PrimaryPurple else Color.LightGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

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
}

@Composable
fun RatingBar(rating: Float, reviewCount: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("$rating", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
        Text(" ($reviewCount)", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun Price(price: Double, oldPrice: Double?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = PriceUtils.formatPriceCLP(price),
            fontWeight = FontWeight.Bold,
            color = PrimaryPurple,
            fontSize = 18.sp
        )
        if (oldPrice != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = PriceUtils.formatPriceCLP(oldPrice),
                color = Color.Gray,
                fontSize = 14.sp,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}
