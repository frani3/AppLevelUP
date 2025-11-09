package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.components.DetailPrice
import com.applevelup.levepupgamerapp.presentation.ui.components.DetailQuantityControl
import com.applevelup.levepupgamerapp.presentation.ui.components.ProductDescription
import com.applevelup.levepupgamerapp.presentation.ui.components.ProductHeader
import com.applevelup.levepupgamerapp.presentation.ui.components.ProductImageCarousel
import com.applevelup.levepupgamerapp.presentation.ui.components.ProductDetailTopBar
import com.applevelup.levepupgamerapp.presentation.ui.components.ReviewsSection
import com.applevelup.levepupgamerapp.presentation.ui.theme.*
import com.applevelup.levepupgamerapp.utils.PriceUtils
import com.applevelup.levepupgamerapp.presentation.viewmodel.CartViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cartViewModel: CartViewModel = viewModel()
    val cartState by cartViewModel.uiState.collectAsState()
    val cartCount = cartState.items.sumOf { it.quantity }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryPurple)
        }
        return
    }

    val product = state.product
    if (product == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Producto no encontrado", color = Color.White)
        }
        return
    }

    LaunchedEffect(state.addedToCart) {
        if (state.addedToCart) {
            snackbarHostState.showSnackbar("Producto agregado al carrito")
            viewModel.consumeAddedToCartFlag()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ProductDetailTopBar(
                title = product.name,
                cartCount = cartCount,
                onBackClick = { navController.popBackStack() },
                onCartClick = { navController.navigate("carrito") }
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = TopBarAndDrawerColor) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Total", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        PriceUtils.formatPriceCLP(state.totalPrice),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Button(
                    onClick = { viewModel.addCurrentSelectionToCart() },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Agregar al Carrito", fontSize = 16.sp)
                }
            }
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { ProductImageCarousel(product = product) }
            item {
                ProductHeader(
                    product = product,
                    isFavorite = state.isFavorite,
                    onToggleFavorite = viewModel::toggleFavorite
                )
            }
            item { DetailPrice(price = product.price, oldPrice = product.oldPrice) }
            item {
                DetailQuantityControl(
                    quantity = state.quantity,
                    onIncrease = viewModel::increaseQuantity,
                    onDecrease = viewModel::decreaseQuantity
                )
            }
            item { ProductDescription(product.description) }
            item { ReviewsSection(state.reviews) }
        }
    }
}
