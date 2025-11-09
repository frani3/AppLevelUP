package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.components.*
import com.applevelup.levepupgamerapp.presentation.ui.theme.*
import com.applevelup.levepupgamerapp.presentation.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
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
            if (state.items.isNotEmpty()) {
                CartBottomBar(
                    subtotal = state.subtotal,
                    shippingCost = state.shippingCost,
                    total = state.total,
                    onCheckout = { navController.navigate(Destinations.Checkout.route) }
                )
            }
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        if (state.items.isEmpty()) {
            EmptyCartView(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.items, key = { it.id }) { item ->
                    CartItemCard(
                        item = item,
                        onQuantityIncrease = { viewModel.updateQuantity(item.id, item.quantity + 1) },
                        onQuantityDecrease = {
                            if (item.quantity > 1) viewModel.updateQuantity(item.id, item.quantity - 1)
                        },
                        onDelete = { viewModel.removeItem(item.id) }
                    )
                }
            }
        }
    }
}

