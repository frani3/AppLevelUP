package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImageCarousel(product: Product) {
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
                contentDescription = "Imagen del producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { index ->
                val color = if (pagerState.currentPage == index)
                    PrimaryPurple else Color.LightGray.copy(alpha = 0.5f)
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

@Composable
fun ProductHeader(product: Product, isFavorite: Boolean, onToggleFavorite: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow)
                Text("${product.rating}", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        IconButton(onClick = onToggleFavorite) {
            Icon(
                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorito",
                tint = if (isFavorite) PrimaryPurple else Color.Gray
            )
        }
    }
}

@Composable
fun DetailPrice(price: Double, oldPrice: Double?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "$${"%.2f".format(price)}",
            fontWeight = FontWeight.Bold,
            color = PrimaryPurple,
            fontSize = 24.sp
        )
        if (oldPrice != null) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = "$${"%.2f".format(oldPrice)}",
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}

@Composable
fun DetailQuantityControl(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .background(Color.DarkGray.copy(alpha = 0.3f), CircleShape)
    ) {
        IconButton(onClick = onDecrease) {
            Text("-", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 8.dp))
        }
        Text(quantity.toString(), color = Color.White, fontWeight = FontWeight.Bold)
        IconButton(onClick = onIncrease) {
            Text("+", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}

@Composable
fun ExpandableDescription() {
    var expanded by remember { mutableStateOf(false) }
    val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse potenti. Cras in sapien sit amet mauris dignissim fermentum."

    Column(Modifier.padding(16.dp)) {
        Text("Descripción", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.animateContentSize()) {
            Text(
                text = description,
                color = Color.LightGray,
                lineHeight = 22.sp,
                maxLines = if (expanded) Int.MAX_VALUE else 3
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
