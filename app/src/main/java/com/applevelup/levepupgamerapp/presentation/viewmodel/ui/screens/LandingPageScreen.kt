package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
// --- Para el Carrusel (Pager) ---
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

// --- Para los Chips de Categoría ---
import androidx.compose.material3.SuggestionChipDefaults

// --- COLORES PRINCIPALES DEL DISEÑO ---
val PureBlackBackground = Color.Black
val TopBarAndDrawerColor = Color.Black.copy(alpha = 0.85f)
val PrimaryPurple = Color(0xFFC000FF)
val CardBackgroundColor = Color(0xFF2C2C2C) // Un poco más opaco para mejor legibilidad

// --- DATOS DE EJEMPLO (Esto vendría de tu ViewModel) ---
data class Product(val name: String, val price: String, val imageRes: Int)
data class Category(val name: String, val icon: @Composable () -> Unit)
data class Promotion(val title: String, val subtitle: String, val imageRes: Int)

val samplePromotions = listOf(
    Promotion("OFERTAS DE OTOÑO", "Hasta 40% en periféricos", R.drawable.promo_banner_2),
    Promotion("NUEVOS MONITORES", "Descubre la resolución 4K", R.drawable.promo_banner_1),
    Promotion("SILLAS GAMER PRO", "Comodidad para largas sesiones", R.drawable.promo_banner_2)
)

val sampleCategories = listOf(
    Category("Teclados") { Icon(Icons.Default.Keyboard, null) },
    Category("Mouse") { Icon(Icons.Default.Mouse, null) },
    Category("Sillas") { Icon(Icons.Default.Chair, null) },
    Category("Monitores") { Icon(Icons.Default.Monitor, null) },
    Category("Headsets") { Icon(Icons.Default.Headset, null) }
)

val sampleFeaturedProducts = listOf(
    Product("Teclado Mecánico RGB", "$99.990", R.drawable.teclado_product),
    Product("Mouse Gamer Inalámbrico", "$64.990", R.drawable.mouse_product),
    Product("Silla Gamer Ergonómica", "$199.990", R.drawable.silla_product),
    Product("Notebook Gamer Asus", "$249.990", R.drawable.pc_product)
)

val sampleNewProducts = listOf(
    Product("Headset 7.1 Surround", "$89.990", R.drawable.audifonos_product),
    Product("Mousepad XXL", "$29.990", R.drawable.mousepad_product),
    Product("Polerón LevelUp", "$149.990", R.drawable.poleron_product),
    Product("Micrófono Condensador", "$119.990", R.drawable.polera_product)
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPageScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { AppDrawerContent(navController = navController) }
    ) {
        Scaffold(
            topBar = {
                LandingPageTopBar(
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = PureBlackBackground
        ) { paddingValues ->
            // --- NUEVO CONTENIDO CON SCROLL VERTICAL ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Barra de Búsqueda
                item {
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        SearchBar()
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Carrusel de Promociones
                item {
                    FeaturedCarousel(promotions = samplePromotions)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Título de Sección: Categorías
                item {
                    SectionTitle("Categorías Populares", Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Chips de Categorías
                item {
                    CategoryChips(categories = sampleCategories)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Título de Sección: Destacados
                item {
                    SectionTitle("Productos Destacados", Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Fila de Productos Destacados
                item {
                    ProductShowcaseRow(products = sampleFeaturedProducts)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Título de Sección: Nuevos
                item {
                    SectionTitle("Nuevos Lanzamientos", Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Fila de Nuevos Productos
                item {
                    ProductShowcaseRow(products = sampleNewProducts)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// --- NUEVOS COMPONENTES DE LA LANDING PAGE ---

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedCarousel(promotions: List<Promotion>) {
    val pagerState = rememberPagerState(pageCount = { promotions.size })

    // Auto-scroll
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000) // Espera 5 segundos
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        pageSpacing = 16.dp
    ) { page ->
        val promotion = promotions[page]
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { /* Navegar a la promoción */ },
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = promotion.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradiente oscuro para legibilidad del texto
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 400f
                        )
                    )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = promotion.title, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
                    Text(text = promotion.subtitle, fontSize = 16.sp, color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.White,
        modifier = modifier
    )
}

@Composable
fun CategoryChips(categories: List<Category>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            SuggestionChip(
                onClick = { /* Navegar a la categoría */ },
                label = { Text(category.name) },
                icon = category.icon,
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = CardBackgroundColor,
                    labelColor = Color.White,
                    iconContentColor = Color.White
                )
            )
        }
    }
}

@Composable
fun ProductShowcaseRow(products: List<Product>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product)
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .width(160.dp) // Ancho fijo para cada tarjeta
            .clickable { /* Navegar al producto */ },
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
                    .aspectRatio(1f) // Imagen cuadrada
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PrimaryPurple
                )
            }
        }
    }
}


// --- COMPONENTES ANTIGUOS QUE SE MANTIENEN (TopBar, SearchBar, Drawer) ---
// (El código de LandingPageTopBar, SearchBar y AppDrawerContent va aquí, sin cambios)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPageTopBar(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TopBarAndDrawerColor,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú"
                )
            }
        },
        title = {
            Image(
                painter = painterResource(id = R.drawable.logo_levelup),
                contentDescription = "Logo",
                modifier = Modifier.height(30.dp)
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate("perfil") }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil"
                )
            }
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = PrimaryPurple,
                        contentColor = Color.White
                    ) {
                        Text(text = "4")
                    }
                }
            ) {
                IconButton(onClick = { navController.navigate("carrito") }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrito"
                    )
                }
            }
        }
    )
}

@Composable
fun SearchBar() {
    var query by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Buscar productos...", color = Color.Gray) },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = CardBackgroundColor,
            unfocusedContainerColor = CardBackgroundColor,
            disabledContainerColor = CardBackgroundColor,
            focusedIndicatorColor = PrimaryPurple,
            unfocusedIndicatorColor = PrimaryPurple,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true,
        trailingIcon = {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryPurple)
                    .clickable { /* TODO: Lógica de búsqueda */ }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.White
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerContent(navController: NavController) {
    var categoriesExpanded by rememberSaveable { mutableStateOf(false) }
    val subCategories = listOf(
        "JUEGOS DE MESA", "ACCESORIOS", "CONSOLAS", "COMPUTADORES GAMERS", "SILLAS GAMERS",
        "MOUSE", "MOUSEPAD", "POLERAS PERSONALIZADAS", "POLERONES GAMERS PERSONALIZADOS"
    )

    ModalDrawerSheet(
        drawerContainerColor = TopBarAndDrawerColor,
        drawerContentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp)
        ) {
            NavigationDrawerItem(
                label = { Text("INICIO", fontWeight = FontWeight.SemiBold) },
                selected = false,
                onClick = { navController.navigate("landing_page") },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedTextColor = Color.White
                )
            )
            NavigationDrawerItem(
                label = { Text("CATEGORÍAS", fontWeight = FontWeight.SemiBold) },
                selected = categoriesExpanded,
                onClick = { categoriesExpanded = !categoriesExpanded },
                icon = {
                    val rotation by animateFloatAsState(targetValue = if (categoriesExpanded) 180f else 0f, label = "rotation")
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expandir",
                        modifier = Modifier.rotate(rotation)
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    selectedContainerColor = Color.Transparent,
                    unselectedTextColor = Color.White,
                    selectedTextColor = Color.White,
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .border(
                        width = if (categoriesExpanded) 2.dp else 0.dp,
                        color = if (categoriesExpanded) PrimaryPurple else Color.Transparent,
                        shape = RoundedCornerShape(100)
                    )
            )
            AnimatedVisibility(visible = categoriesExpanded) {
                Column(modifier = Modifier.padding(start = 24.dp)) {
                    subCategories.forEach { categoryName ->
                        Text(
                            text = categoryName,
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* navController.navigate("categoria/$categoryName") */ }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }
            }
            NavigationDrawerItem(
                label = { Text("NOSOTROS", fontWeight = FontWeight.SemiBold) },
                selected = false,
                onClick = { /* TODO: navController.navigate("nosotros") */ },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedTextColor = Color.White
                )
            )
            NavigationDrawerItem(
                label = { Text("COMUNIDAD", fontWeight = FontWeight.SemiBold) },
                selected = false,
                onClick = { /* TODO: navController.navigate("comunidad") */ },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedTextColor = Color.White
                )
            )
            NavigationDrawerItem(
                label = { Text("CONTACTO", fontWeight = FontWeight.SemiBold) },
                selected = false,
                onClick = { /* TODO: navController.navigate("contacto") */ },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}