@file:OptIn(ExperimentalMaterial3Api::class)

package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.model.CardType
import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.components.SummarySection
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.viewmodel.CheckoutUiState
import com.applevelup.levepupgamerapp.presentation.viewmodel.CheckoutViewModel
import com.applevelup.levepupgamerapp.utils.PriceUtils
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    viewModel: CheckoutViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshSelections()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
    }

    val isConfirmEnabled = state.items.isNotEmpty() &&
        state.selectedAddress != null &&
        state.selectedPayment != null &&
        !state.isProcessing

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryPurple,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        if (state.items.isEmpty()) {
            EmptyCheckoutView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                navController.navigate(Destinations.Landing.route) {
                    popUpTo(Destinations.Landing.route) { inclusive = false }
                }
            }
        } else {
            CheckoutContent(
                state = state,
                paddingValues = paddingValues,
                isConfirmEnabled = isConfirmEnabled,
                onManageAddress = { navController.navigate(Destinations.Addresses.route) },
                onManagePayment = { navController.navigate(Destinations.PaymentMethods.route) },
                onConfirm = {
                    viewModel.confirmPurchase { order, total ->
                        val route = Destinations.OrderSuccess.create(order.id, total.roundToInt())
                        navController.navigate(route) {
                            popUpTo(Destinations.Cart.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun CheckoutContent(
    state: CheckoutUiState,
    paddingValues: PaddingValues,
    isConfirmEnabled: Boolean,
    onManageAddress: () -> Unit,
    onManagePayment: () -> Unit,
    onConfirm: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.isProcessing) {
            item {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryPurple
                )
            }
        }

        item {
            CheckoutSectionCard(
                title = "Dirección de entrega",
                actionLabel = if (state.selectedAddress == null) "Agregar" else "Cambiar",
                onActionClick = onManageAddress
            ) {
                AddressSummary(state.selectedAddress, state.isLoadingSelections)
            }
        }

        item {
            CheckoutSectionCard(
                title = "Método de pago",
                actionLabel = if (state.selectedPayment == null) "Agregar" else "Cambiar",
                onActionClick = onManagePayment
            ) {
                PaymentSummary(state.selectedPayment, state.isLoadingSelections)
            }
        }

        item {
            CheckoutSectionCard(title = "Resumen de productos") {
                state.items.forEach { item ->
                    CheckoutItemRow(item)
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                }
            }
        }

        item {
            SummarySection(
                subtotal = state.subtotal,
                shippingCost = state.shippingCost,
                total = state.total
            )
        }

        item {
            Button(
                onClick = onConfirm,
                enabled = isConfirmEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                if (state.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Confirmar compra", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun CheckoutSectionCard(
    title: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B1F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                if (actionLabel != null && onActionClick != null) {
                    TextButton(onClick = onActionClick) {
                        Text(actionLabel, color = PrimaryPurple)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun AddressSummary(address: Address?, isLoading: Boolean) {
    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple, strokeWidth = 3.dp)
            }
        }
        address == null -> {
            Text(
                text = "No tienes direcciones guardadas. Agrega una para continuar.",
                color = Color.Gray
            )
        }
        else -> {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = PrimaryPurple)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(address.alias, color = Color.White, fontWeight = FontWeight.Bold)
                    if (address.isDefault) {
                        Spacer(modifier = Modifier.width(8.dp))
                        AssistChip(onClick = {}, enabled = false, label = { Text("Predeterminada") })
                    }
                }
                Text(address.street, color = Color.LightGray)
                val details = address.details.takeIf { it.isNotBlank() }
                val secondary = listOfNotNull(address.city, details).joinToString(" · ")
                Text(secondary, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun PaymentSummary(payment: PaymentMethod?, isLoading: Boolean) {
    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple, strokeWidth = 3.dp)
            }
        }
        payment == null -> {
            Text(
                text = "No tienes métodos de pago activos. Agrega uno para pagar.",
                color = Color.Gray
            )
        }
        else -> {
            val cardLabel = when (payment.cardType) {
                CardType.VISA -> "Visa"
                CardType.MASTERCARD -> "MasterCard"
                CardType.OTHER -> "Tarjeta"
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Payment, contentDescription = null, tint = PrimaryPurple)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(cardLabel, color = Color.White, fontWeight = FontWeight.Bold)
                    if (payment.isDefault) {
                        Spacer(modifier = Modifier.width(8.dp))
                        AssistChip(onClick = {}, enabled = false, label = { Text("Predeterminada") })
                    }
                }
                Text("**** **** **** ${payment.lastFourDigits}", color = Color.LightGray)
                Text("Vence: ${payment.expiryDate}", color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun CheckoutItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text("Cantidad: ${item.quantity}", color = Color.Gray, fontSize = 13.sp)
        }
        Text(PriceUtils.formatPriceCLP(item.price * item.quantity), color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EmptyCheckoutView(modifier: Modifier = Modifier, onBrowse: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCartCheckout,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("No tienes productos para pagar", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Explora el catálogo y agrega artículos para continuar.",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(onClick = onBrowse) {
            Text("Ir al catálogo", color = PrimaryPurple)
        }
    }
}
