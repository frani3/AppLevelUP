package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpProduct
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpCartRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpOrderRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAddressRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpUserRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpProductRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.CreateOrderInput
import com.applevelup.levepupgamerapp.domain.repository.levelup.OrderItemInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

data class CheckoutUiState(
    val items: List<CartItem> = emptyList(),
    val selectedAddress: Address? = null,
    val selectedPayment: PaymentMethod? = null,
    val subtotal: Double = 0.0,
    val shippingCost: Double = 0.0,
    val total: Double = 0.0,
    val isProcessing: Boolean = false,
    val isLoadingSelections: Boolean = true,
    val errorMessage: String? = null
)

class CheckoutViewModel(
    private val cartRepository: LevelUpCartRepository = LevelUpDependencyContainer.cartRepository,
    private val orderRepository: LevelUpOrderRepository = LevelUpDependencyContainer.orderRepository,
    private val addressRepository: LevelUpAddressRepository = LevelUpDependencyContainer.addressRepository,
    private val userRepository: LevelUpUserRepository = LevelUpDependencyContainer.userRepository,
    private val productRepository: LevelUpProductRepository = LevelUpDependencyContainer.productRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    // Métodos de pago disponibles (configuración fija - la API no maneja métodos de pago)
    private val availablePaymentMethods = listOf(
        PaymentMethod(id = 1, cardType = com.applevelup.levepupgamerapp.domain.model.CardType.OTHER, cardBrand = "Tarjeta de Crédito/Débito", lastFourDigits = "****", isDefault = true),
        PaymentMethod(id = 2, cardType = com.applevelup.levepupgamerapp.domain.model.CardType.TRANSFER, cardBrand = "Transferencia Bancaria", lastFourDigits = "****", isDefault = false)
    )

    init {
        observeCartWithProducts()
        refreshSelections()
    }

    private fun observeCartWithProducts() {
        viewModelScope.launch {
            combine(
                cartRepository.observeCart(),
                productRepository.observeProducts()
            ) { cart, products ->
                Pair(cart, products)
            }.collectLatest { (cart, products) ->
                val productMap = products.associateBy { it.code }
                val items = cart.items.mapNotNull { item ->
                    val product = productMap[item.productCode]
                    if (product != null) {
                        CartItem(
                            id = item.productCode.hashCode(),
                            name = product.name,
                            price = product.price,
                            imageUrl = product.imageUrl,
                            quantity = item.quantity
                        )
                    } else {
                        CartItem(
                            id = item.productCode.hashCode(),
                            name = "Producto: ${item.productCode}",
                            price = 0.0,
                            imageUrl = null,
                            quantity = item.quantity
                        )
                    }
                }
                updateTotals(items)
            }
        }
    }

    private fun updateTotals(items: List<CartItem>) {
        val subtotal = items.sumOf { it.price * it.quantity }
        val shipping = if (subtotal > 0) SHIPPING_FEE_CLP else 0.0
        val total = subtotal + shipping
        _uiState.update { state ->
            state.copy(
                items = items,
                subtotal = subtotal,
                shippingCost = shipping,
                total = total
            )
        }
    }

    fun refreshSelections() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSelections = true) }
            
            // Cargar carrito y productos desde API
            productRepository.refreshProducts(force = false)
            cartRepository.refreshCart()

            val addressesResult = runCatching { withContext(Dispatchers.IO) { loadLevelUpAddresses() } }

            addressesResult.onSuccess { addresses ->
                _uiState.update { state ->
                    state.copy(
                        selectedAddress = addresses.firstOrNull { it.isDefault } ?: addresses.firstOrNull(),
                        selectedPayment = availablePaymentMethods.firstOrNull { it.isDefault } ?: availablePaymentMethods.firstOrNull(),
                        isLoadingSelections = false,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        selectedAddress = null,
                        selectedPayment = availablePaymentMethods.firstOrNull { it.isDefault } ?: availablePaymentMethods.firstOrNull(),
                        isLoadingSelections = false,
                        errorMessage = error.message ?: "No pudimos cargar tus direcciones"
                    )
                }
            }
        }
    }

    fun confirmPurchase(onSuccess: (Order, Double) -> Unit) {
        val currentState = _uiState.value
        if (currentState.isProcessing) return

        if (currentState.items.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Tu carrito está vacío") }
            return
        }

        val address = currentState.selectedAddress
        if (address == null) {
            _uiState.update { it.copy(errorMessage = "Agrega una dirección de entrega") }
            return
        }

        val payment = currentState.selectedPayment
        if (payment == null) {
            _uiState.update { it.copy(errorMessage = "Selecciona un método de pago") }
            return
        }

        val itemsSnapshot = currentState.items
        val totalBeforeOrder = currentState.total

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, errorMessage = null) }
            try {
                delay(PAYMENT_SIMULATION_DELAY_MS)
                
                // Crear orden usando LevelUp API
                val orderItems = itemsSnapshot.map { item ->
                    com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpOrderItem(
                        productCode = resolveProductCode(item.id),
                        name = item.name,
                        quantity = item.quantity,
                        unitPrice = item.price,
                        imageUrl = item.imageUrl
                    )
                }
                
                val paymentMethodString = when (payment.id) {
                    1 -> "tarjeta"
                    2 -> "transferencia"
                    else -> "tarjeta"
                }
                
                val orderItemInputs = orderItems.map { item ->
                    OrderItemInput(
                        productCode = item.productCode,
                        name = item.name,
                        quantity = item.quantity,
                        unitPrice = item.unitPrice
                    )
                }
                
                val createResult = orderRepository.createOrder(
                    CreateOrderInput(
                        items = orderItemInputs,
                        address = address.street,
                        region = extractRegion(address.city),
                        commune = extractComuna(address.city),
                        paymentMethod = paymentMethodString
                    )
                )
                
                when (createResult) {
                    is LevelUpResult.Success -> {
                        // Limpiar carrito después de crear orden
                        cartRepository.clearCart()
                        
                        // Convertir a modelo legacy para callback
                        val legacyOrder = Order(
                            id = createResult.data.id,
                            date = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
                            total = "$${String.format("%.2f", createResult.data.total)}",
                            status = createResult.data.status,
                            itemCount = itemsSnapshot.size
                        )
                        
                        _uiState.update { it.copy(isProcessing = false) }
                        onSuccess(legacyOrder, totalBeforeOrder)
                    }
                    is LevelUpResult.Failure -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                errorMessage = createResult.throwable.message ?: "Error al crear orden"
                            )
                        }
                    }
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        errorMessage = error.message ?: "Error al procesar el pago"
                    )
                }
            }
        }
    }
    
    private suspend fun resolveProductCode(itemId: Int): String {
        // Buscar el productCode real del carrito
        val cart = cartRepository.observeCart().first()
        return cart.items.find { it.productCode.hashCode() == itemId }?.productCode 
            ?: "PROD-$itemId"
    }
    
    private fun extractRegion(city: String): String {
        // city format: "comuna · region" o solo "comuna"
        return city.split(" · ").getOrNull(1)?.trim() ?: city
    }
    
    private fun extractComuna(city: String): String {
        // city format: "comuna · region" o solo "comuna"
        return city.split(" · ").getOrNull(0)?.trim() ?: city
    }

    companion object {
        private const val SHIPPING_FEE_CLP = 5990.0
        private const val PAYMENT_SIMULATION_DELAY_MS = 1200L
    }

    private suspend fun loadLevelUpAddresses(): List<Address> {
        val run = resolveUserRun()
        val refresh = addressRepository.refreshAddresses(run, force = true)
        if (refresh is LevelUpResult.Failure) {
            throw refresh.throwable
        }
        return addressRepository.observeAddresses(run).first().map { it.toLegacyAddress() }
    }

    private suspend fun resolveUserRun(): String {
        val cachedProfile = userRepository.observeProfile().firstOrNull()
        val profile = when {
            cachedProfile != null -> cachedProfile
            else -> when (val refresh = userRepository.refreshProfile()) {
                is LevelUpResult.Success -> refresh.data
                is LevelUpResult.Failure -> throw refresh.throwable
            }
        }
        return profile.run.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("No encontramos tu RUN LevelUp")
    }
}

private fun LevelUpAddress.toLegacyAddress(): Address {
    val computedId = id.toIntOrNull() ?: id.hashCode()
    val streetLine = buildString {
        append(street)
        numero?.takeIf { it.isNotBlank() }?.let {
            append(" #").append(it)
        }
    }
    val cityLine = listOfNotNull(
        comuna.takeIf { it.isNotBlank() },
        region.takeIf { it.isNotBlank() }
    ).joinToString(separator = " · ")

    return Address(
        id = computedId,
        alias = alias.ifBlank { "Dirección" },
        street = streetLine,
        city = cityLine,
        details = complement.orEmpty(),
        isDefault = isPrimary
    )
}
