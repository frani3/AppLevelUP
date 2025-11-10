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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
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
import com.applevelup.levepupgamerapp.presentation.ui.components.LevelUpBottomNavigation
import com.applevelup.levepupgamerapp.presentation.ui.components.MainDestination
import com.applevelup.levepupgamerapp.presentation.ui.components.ProductShowcaseRow
import com.applevelup.levepupgamerapp.presentation.ui.components.SearchSuggestionPanel
import com.applevelup.levepupgamerapp.presentation.ui.components.SectionTitle
import com.applevelup.levepupgamerapp.presentation.ui.components.mapRouteToMainDestination
import com.applevelup.levepupgamerapp.presentation.ui.components.navigateToMainDestination
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.viewmodel.CartViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.LandingViewModel

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

    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = remember(backStackEntry?.destination?.route) {
        mapRouteToMainDestination(backStackEntry?.destination?.route)
    }

    BackHandler(enabled = searchActive) {
        searchActive = false
        searchQuery = ""
    }

    fun submitSearch(query: String) {
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
                onSearchSubmit = { query -> submitSearch(query) },
                notificationCount = notificationCount,
                onNotificationClick = {
                    navController.navigate(Destinations.Notifications.route)
                }
            )
        },
        bottomBar = {
            LevelUpBottomNavigation(
                selectedDestination = selectedDestination,
                onDestinationSelected = { destination ->
                    if (destination == selectedDestination) return@LevelUpBottomNavigation

                    searchActive = false
                    searchQuery = ""

                    navController.navigateToMainDestination(destination)
                },
                cartBadgeCount = cartCount
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
                        onSuggestionClick = { suggestion -> submitSearch(suggestion) },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = paddingValues.calculateTopPadding() + 16.dp)
                    )
                }
            }
        }
    }
}
