package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.AddressRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.CartRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.PaymentRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.SessionRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.UserRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.Address
import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.model.Order
import com.applevelup.levepupgamerapp.domain.model.PaymentMethod
import com.applevelup.levepupgamerapp.domain.repository.AddressRepository
import com.applevelup.levepupgamerapp.domain.repository.CartRepository
import com.applevelup.levepupgamerapp.domain.repository.PaymentRepository
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import com.applevelup.levepupgamerapp.domain.repository.UserRepository
import com.applevelup.levepupgamerapp.domain.usecase.CreateOrderUseCase
import com.applevelup.levepupgamerapp.domain.usecase.GetCartUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private val cartRepository: CartRepository = CartRepositoryImpl(),
    private val sessionRepository: SessionRepository = SessionRepositoryImpl(),
    private val paymentRepository: PaymentRepository = PaymentRepositoryImpl(),
    private val addressRepository: AddressRepository = AddressRepositoryImpl(),
    private val userRepository: UserRepository = UserRepositoryImpl(),
    private val getCartUseCase: GetCartUseCase = GetCartUseCase(cartRepository, sessionRepository),
    private val createOrderUseCase: CreateOrderUseCase = CreateOrderUseCase(cartRepository, sessionRepository, userRepository)
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    init {
        observeCart()
        refreshSelections()
    }

    private fun observeCart() {
        viewModelScope.launch {
            getCartUseCase().collectLatest { items ->
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
            val addresses = withContext(Dispatchers.Default) { addressRepository.getAddresses() }
            val payments = withContext(Dispatchers.Default) { paymentRepository.getPaymentMethods() }

            _uiState.update { state ->
                state.copy(
                    selectedAddress = addresses.firstOrNull { it.isDefault } ?: addresses.firstOrNull(),
                    selectedPayment = payments.firstOrNull { it.isDefault } ?: payments.firstOrNull(),
                    isLoadingSelections = false,
                    errorMessage = null
                )
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
            _uiState.update { it.copy(errorMessage = "Agrega un método de pago") }
            return
        }

        val itemsSnapshot = currentState.items
        val shippingSnapshot = currentState.shippingCost
        val totalBeforeOrder = currentState.total

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, errorMessage = null) }
            try {
                delay(PAYMENT_SIMULATION_DELAY_MS)
                val order = createOrderUseCase(
                    items = itemsSnapshot,
                    shippingCost = shippingSnapshot,
                    address = address,
                    paymentMethod = payment
                )
                _uiState.update { it.copy(isProcessing = false) }
                onSuccess(order, totalBeforeOrder)
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

    companion object {
        private const val SHIPPING_FEE_CLP = 5990.0
        private const val PAYMENT_SIMULATION_DELAY_MS = 1200L
    }
}
