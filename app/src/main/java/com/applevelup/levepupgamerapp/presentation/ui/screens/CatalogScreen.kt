package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.components.LevelUpBottomNavigation
import com.applevelup.levepupgamerapp.presentation.ui.components.MainDestination
import com.applevelup.levepupgamerapp.presentation.ui.components.ProductFilterSheet
import com.applevelup.levepupgamerapp.presentation.ui.components.ProductListItem
import com.applevelup.levepupgamerapp.presentation.ui.components.mapRouteToMainDestination
import com.applevelup.levepupgamerapp.presentation.ui.components.navigateToMainDestination
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.ui.theme.TopBarAndDrawerColor
import com.applevelup.levepupgamerapp.presentation.viewmodel.CartViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.CatalogViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.CatalogViewModelFactory
import com.applevelup.levepupgamerapp.presentation.viewmodel.ProductListEvent
import com.applevelup.levepupgamerapp.presentation.viewmodel.SessionViewModel
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    viewModel: CatalogViewModel = viewModel(factory = CatalogViewModelFactory()),
    cartViewModel: CartViewModel = viewModel(),
    sessionViewModel: SessionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showFilterSheet by remember { mutableStateOf(false) }

    val cartState by cartViewModel.uiState.collectAsState()
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val cartCount = remember(cartState.items) { cartState.items.sumOf { it.quantity } }
    val isAdminUser = remember(sessionState) {
        sessionState.isSuperAdmin || sessionState.profileRole?.equals("Administrador", ignoreCase = true) == true
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = mapRouteToMainDestination(backStackEntry?.destination?.route)

    val openAddProduct = remember(navController, isAdminUser) {
        {
            if (!isAdminUser) {
                navController.navigateToMainDestination(MainDestination.Cart)
            } else {
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (currentRoute != Destinations.AddProduct.route) {
                    navController.navigate(Destinations.AddProduct.route)
                }
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is ProductListEvent.ItemAdded) {
                snackbarHostState.showSnackbar("Producto agregado al carrito")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filtros", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            LevelUpBottomNavigation(
                selectedDestination = selectedDestination,
                onDestinationSelected = { destination ->
                    if (destination == selectedDestination) return@LevelUpBottomNavigation
                    navController.navigateToMainDestination(destination)
                },
                cartBadgeCount = cartCount,
                isAdmin = isAdminUser,
                onCentralAction = openAddProduct
            )
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple)
            }

            uiState.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.errorMessage}", color = Color.Red)
            }

            uiState.products.isEmpty() -> CatalogEmptyState()

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val activeFilters = uiState.filters.activeFiltersCount()
                FilterSummaryBar(
                    total = uiState.products.size,
                    activeFilters = activeFilters,
                    onOpenFilters = { showFilterSheet = true }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.products, key = { it.id }) { product ->
                        ProductListItem(
                            product = product,
                            onAddToCart = { viewModel.addProductToCart(product.id) },
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        ProductFilterSheet(
            currentFilters = uiState.filters,
            availablePriceRange = uiState.availablePriceRange,
            onApply = viewModel::applyFilters,
            onReset = viewModel::resetFilters,
            onDismiss = { showFilterSheet = false },
            availableCategories = uiState.availableCategories
        )
    }
}

@Composable
private fun FilterSummaryBar(total: Int, activeFilters: Int, onOpenFilters: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$total productos",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodyMedium
        )

        AssistChip(
            onClick = onOpenFilters,
            label = {
                Text(
                    text = if (activeFilters > 0) "Filtros ($activeFilters)" else "Filtros",
                    color = Color.White
                )
            },
            leadingIcon = {
                Icon(Icons.Filled.FilterList, contentDescription = null, tint = Color.White)
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = Color.DarkGray.copy(alpha = 0.4f)
            )
        )
    }
}

@Composable
private fun CatalogEmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("¡Uy!", fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.headlineSmall)
            Text(
                "No encontramos productos disponibles en este momento.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
