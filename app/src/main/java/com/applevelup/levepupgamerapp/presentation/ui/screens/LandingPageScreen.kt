package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.components.CategoryShortcuts
import com.applevelup.levepupgamerapp.presentation.ui.components.FeaturedCarousel
import com.applevelup.levepupgamerapp.presentation.ui.components.LandingPageTopBar
import com.applevelup.levepupgamerapp.presentation.ui.components.ProductShowcaseRow
import com.applevelup.levepupgamerapp.presentation.ui.components.SearchSuggestionPanel
import com.applevelup.levepupgamerapp.presentation.ui.components.SectionTitle
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.viewmodel.CartViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.LandingViewModel

private enum class MainDestination { Home, Categories, Cart, Favorites, Profile }

private data class BottomNavItem(
    val destination: MainDestination,
    val route: String,
    val label: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val isCentral: Boolean = false,
    val badgeCount: Int = 0
)

@Composable
fun LandingPageScreen(
    navController: NavController,
    landingViewModel: LandingViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val landingState by landingViewModel.uiState.collectAsState()
    val cartState by cartViewModel.uiState.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchActive by rememberSaveable { mutableStateOf(false) }
    val recentSearches = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { saved -> mutableStateListOf(*saved.toTypedArray()) }
        )
    ) {
        mutableStateListOf(
            "PlayStation 5",
            "Nintendo Switch OLED",
            "RTX 4070",
            "Silla gamer ergonómica"
        )
    }

    val trendingSuggestions = remember {
        listOf(
            "Auriculares HyperX Cloud III",
            "Steam Deck OLED",
            "Monitor 240Hz",
            "Teclado mecánico low profile"
        )
    }

    val cartCount = remember(cartState.items) { cartState.items.sumOf { it.quantity } }
    val notificationCount = remember { 3 }

    val contentAlpha by animateFloatAsState(
        targetValue = if (searchActive) 0.15f else 1f,
        label = "contentAlpha"
    )
    val overlayAlpha by animateFloatAsState(
        targetValue = if (searchActive) 0.85f else 0f,
        label = "overlayAlpha"
    )

    val recentlyViewed = remember(landingState.featured, landingState.newProducts) {
        if (landingState.featured.isNotEmpty()) landingState.featured else landingState.newProducts
    }
    val curatedRecommendations = remember(landingState.newProducts, landingState.featured) {
        if (landingState.newProducts.isNotEmpty()) landingState.newProducts else landingState.featured
    }

    var selectedDestination by rememberSaveable { mutableStateOf(MainDestination.Home) }
    val backStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(backStackEntry?.destination?.route) {
        selectedDestination = when (backStackEntry?.destination?.route) {
            Destinations.Categories.route -> MainDestination.Categories
            Destinations.Cart.route -> MainDestination.Cart
            Destinations.Profile.route -> MainDestination.Profile
            else -> MainDestination.Home
        }
    }

    BackHandler(enabled = searchActive) {
        searchActive = false
        searchQuery = ""
    }

    fun handleSuggestion(query: String) {
        val normalized = query.trim()
        if (normalized.isEmpty()) return

        val existingIndex = recentSearches.indexOfFirst { it.equals(normalized, ignoreCase = true) }
        if (existingIndex >= 0) {
            recentSearches.removeAt(existingIndex)
        }
    recentSearches.add(0, normalized)
    if (recentSearches.size > 6) {
            recentSearches.removeAt(recentSearches.lastIndex)
        }

        searchQuery = ""
        searchActive = false

        navController.navigate(Destinations.Search.withQuery(normalized))
    }

    Scaffold(
        topBar = {
            LandingPageTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { query -> searchQuery = query },
                searchActive = searchActive,
                onSearchActiveChange = { active -> searchActive = active },
                notificationCount = notificationCount,
                onNotificationClick = {
                    navController.navigate(Destinations.Notifications.route)
                }
            )
        },
        bottomBar = {
            val navItems = listOf(
                BottomNavItem(
                    destination = MainDestination.Home,
                    route = Destinations.Landing.route,
                    label = "Inicio",
                    outlinedIcon = Icons.Outlined.Home,
                    filledIcon = Icons.Filled.Home
                ),
                BottomNavItem(
                    destination = MainDestination.Categories,
                    route = Destinations.Categories.route,
                    label = "Categorías",
                    outlinedIcon = Icons.Outlined.Category,
                    filledIcon = Icons.Filled.Category
                ),
                BottomNavItem(
                    destination = MainDestination.Cart,
                    route = Destinations.Cart.route,
                    label = "Carrito",
                    outlinedIcon = Icons.Outlined.ShoppingCart,
                    filledIcon = Icons.Filled.ShoppingCart,
                    isCentral = true,
                    badgeCount = cartCount
                ),
                BottomNavItem(
                    destination = MainDestination.Favorites,
                    route = Destinations.Profile.route,
                    label = "Favoritos",
                    outlinedIcon = Icons.Outlined.Favorite,
                    filledIcon = Icons.Filled.Favorite
                ),
                BottomNavItem(
                    destination = MainDestination.Profile,
                    route = Destinations.Profile.route,
                    label = "Perfil",
                    outlinedIcon = Icons.Outlined.Person,
                    filledIcon = Icons.Filled.Person
                )
            )

            LevelUpBottomNavigation(
                items = navItems,
                selected = selectedDestination,
                onItemSelected = { item ->
                    if (selectedDestination != item.destination) {
                        selectedDestination = item.destination
                    }

                    searchActive = false
                    searchQuery = ""

                    when (item.destination) {
                        MainDestination.Home -> navController.navigate(Destinations.Landing.route) {
                            launchSingleTop = true
                        }
                        MainDestination.Categories -> navController.navigate(Destinations.Categories.route) {
                            launchSingleTop = true
                        }
                        MainDestination.Cart -> navController.navigate(Destinations.Cart.route) {
                            launchSingleTop = true
                        }
                        MainDestination.Favorites -> navController.navigate(Destinations.Profile.route) {
                            launchSingleTop = true
                        }
                        MainDestination.Profile -> navController.navigate(Destinations.Profile.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(contentAlpha),
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + 12.dp,
                    bottom = paddingValues.calculateBottomPadding() + 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    FeaturedCarousel(
                        promotions = landingState.promotions,
                        currentPage = landingState.currentPage,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
                item {
                    SectionTitle(
                        title = "Categorías Populares",
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                item {
                    CategoryShortcuts(
                        categories = landingState.categories,
                        navController = navController
                    )
                }
                item { SectionTitle(title = "Visto recientemente") }
                item {
                    ProductShowcaseRow(
                        products = recentlyViewed,
                        navController = navController
                    )
                }
                item { SectionTitle(title = "Te podría gustar") }
                item {
                    ProductShowcaseRow(
                        products = curatedRecommendations,
                        navController = navController
                    )
                }
                item { SectionTitle(title = "Nuevos lanzamientos") }
                item {
                    ProductShowcaseRow(
                        products = landingState.newProducts,
                        navController = navController
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }

            AnimatedVisibility(
                visible = searchActive,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = overlayAlpha))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                searchActive = false
                                searchQuery = ""
                            }
                    )

                    SearchSuggestionPanel(
                        query = searchQuery,
                        recentItems = recentSearches,
                        trendingItems = trendingSuggestions,
                        onSuggestionClick = { suggestion -> handleSuggestion(suggestion) },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = paddingValues.calculateTopPadding() + 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelUpBottomNavigation(
    items: List<BottomNavItem>,
    selected: MainDestination,
    onItemSelected: (BottomNavItem) -> Unit
) {
    Surface(color = Color(0xFF0B0B0D)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(108.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = selected == item.destination
                val interactionSource = remember(item.destination) { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onItemSelected(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val spacerHeight = if (item.isCentral) 6.dp else 8.dp

                        if (item.isCentral) {
                            val circleSize = if (isSelected) 70.dp else 64.dp
                            val circleColor = if (isSelected) PrimaryPurple else PrimaryPurple.copy(alpha = 0.22f)
                            val iconModifier = Modifier.size(if (isSelected) 26.dp else 24.dp)

                            Surface(
                                modifier = Modifier.size(circleSize),
                                shape = CircleShape,
                                color = circleColor,
                                tonalElevation = if (isSelected) 8.dp else 4.dp,
                                shadowElevation = if (isSelected) 8.dp else 4.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (item.badgeCount > 0) {
                                        BadgedBox(
                                            badge = {
                                                Badge(containerColor = Color.White, contentColor = PrimaryPurple) {
                                                    Text(
                                                        text = item.badgeCount.coerceAtMost(99).toString(),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = item.filledIcon,
                                                contentDescription = item.label,
                                                tint = Color.White,
                                                modifier = iconModifier
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                                            contentDescription = item.label,
                                            tint = Color.White,
                                            modifier = iconModifier
                                        )
                                    }
                                }
                            }
                        } else {
                            val iconModifier = Modifier.size(if (isSelected) 26.dp else 24.dp)
                            if (item.badgeCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = PrimaryPurple) {
                                            Text(
                                                text = item.badgeCount.coerceAtMost(99).toString(),
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                                        contentDescription = item.label,
                                        tint = if (isSelected) PrimaryPurple else Color.White.copy(alpha = 0.7f),
                                        modifier = iconModifier
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                                    contentDescription = item.label,
                                    tint = if (isSelected) PrimaryPurple else Color.White.copy(alpha = 0.7f),
                                    modifier = iconModifier
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(spacerHeight))
                        Text(
                            text = item.label,
                            color = if (isSelected) PrimaryPurple else Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(spacerHeight))
                        Box(
                            modifier = Modifier
                                .height(3.dp)
                                .width(30.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (isSelected) PrimaryPurple else Color.Transparent)
                        )
                    }
                }
            }
        }
    }
}
