package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.*
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground

// 🔹 Top bar principal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPageTopBar(
    navController: NavController,
    onMenuClick: () -> Unit,
    isSearchVisible: Boolean,
    onSearchVisibilityChange: (Boolean) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    fun openSearch() {
        focusManager.clearFocus()
        val trimmed = query.trim()
        val route = if (trimmed.isNotEmpty()) {
            Destinations.Search.withQuery(trimmed)
        } else {
            Destinations.Search.route
        }
        navController.navigate(route)
        query = ""
        onSearchVisibilityChange(false)
    }

    LaunchedEffect(isSearchVisible) {
        if (isSearchVisible) {
            focusRequester.requestFocus()
        } else {
            focusManager.clearFocus()
        }
    }

    Column(modifier = Modifier.background(PureBlackBackground)) {
        CenterAlignedTopAppBar(
            title = { Text("LevelUp Store", fontWeight = FontWeight.Bold, color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = PureBlackBackground
            ),
            actions = {
                if (isSearchVisible) {
                    IconButton(onClick = {
                        query = ""
                        onSearchVisibilityChange(false)
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar búsqueda", tint = Color.White)
                    }
                } else {
                    IconButton(onClick = { onSearchVisibilityChange(true) }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                }
            }
        )
        AnimatedVisibility(visible = isSearchVisible, enter = fadeIn(), exit = fadeOut()) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar productos...") },
                trailingIcon = {
                    IconButton(onClick = { openSearch() }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .focusRequester(focusRequester),
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { openSearch() }),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = PrimaryPurple,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = PrimaryPurple,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = PrimaryPurple,
                    focusedContainerColor = Color.Black.copy(alpha = 0.6f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.4f)
                )
            )
        }
    }
}

// 🔹 Carrusel de promociones
@Composable
fun FeaturedCarousel(promotions: List<Promotion>, currentPage: Int) {
    if (promotions.isEmpty()) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = promotions[currentPage].imageRes),
            contentDescription = promotions[currentPage].title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                promotions[currentPage].title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                promotions[currentPage].subtitle,
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }
    }
}

// 🔹 Título de sección
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

// 🔹 Chips de categorías
@Composable
fun CategoryChips(categories: List<Category>, navController: NavController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            AssistChip(
                onClick = {
                    navController.navigate(Destinations.ProductList.create(category.name))
                },
                label = { Text(category.name, color = Color.White) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = PrimaryPurple.copy(alpha = 0.2f)
                )
            )
        }
    }
}

// 🔹 Carrusel horizontal de productos
@Composable
fun ProductShowcaseRow(
    products: List<ProductSummary>,
    navController: NavController
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(products) { product ->
            Card(
                modifier = Modifier
                    .width(150.dp)
                    .height(210.dp)
                    .clickable {
                        navController.navigate("product_detail/${product.id}")
                    },
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name,
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(product.name, color = Color.White, fontSize = 14.sp, maxLines = 2)
                    Text(
                        product.price,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
