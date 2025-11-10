package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.applevelup.levepupgamerapp.presentation.ui.theme.*
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.components.LevelUpBottomNavigation
import com.applevelup.levepupgamerapp.presentation.ui.components.MainDestination
import com.applevelup.levepupgamerapp.presentation.ui.components.mapRouteToMainDestination
import com.applevelup.levepupgamerapp.presentation.ui.components.navigateToMainDestination
import com.applevelup.levepupgamerapp.presentation.viewmodel.CartViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.CategoryUiState
import com.applevelup.levepupgamerapp.presentation.viewmodel.CategoryViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cartViewModel: CartViewModel = viewModel()
    val cartState by cartViewModel.uiState.collectAsState()
    val sessionViewModel: SessionViewModel = viewModel()
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
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
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple)
            }
        } else {
            CategoryMenuList(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) { categoryName ->
                navController.navigate("productos/$categoryName")
            }
        }
    }
}

@Composable
private fun CategoryMenuList(
    state: CategoryUiState,
    modifier: Modifier = Modifier,
    onCategorySelected: (String) -> Unit
) {
    val categories = remember(state.categories) { state.categories }

    val dividerColor = Color.DarkGray.copy(alpha = 0.5f)

    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(categories, key = { _, category -> category.name }) { index, category ->
            if (index == 0) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = dividerColor,
                    thickness = 0.5.dp
                )
            }
            CategoryMenuItem(
                name = category.name,
                onClick = { onCategorySelected(category.name) }
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = dividerColor,
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun CategoryMenuItem(name: String, onClick: () -> Unit) {
    Text(
        text = name,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        color = Color.White,
        style = MaterialTheme.typography.titleMedium
    )
}
