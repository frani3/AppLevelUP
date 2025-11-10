package com.applevelup.levepupgamerapp.presentation.viewmodel

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.repository.ProductRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.usecase.CreateProductUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val defaultCategories = listOf(
    "Juegos de Mesa",
    "Accesorios",
    "Consolas",
    "Computadores Gamers",
    "Sillas Gamers",
    "Mouse",
    "Mousepad",
    "Poleras Personalizadas",
    "Polerones Gamers Personalizados"
)

private val categoryImageMap = mapOf(
    "Juegos de Mesa" to R.drawable.jm001,
    "Accesorios" to R.drawable.ac001,
    "Consolas" to R.drawable.co001,
    "Computadores Gamers" to R.drawable.cg001,
    "Sillas Gamers" to R.drawable.sg001,
    "Mouse" to R.drawable.ms001,
    "Mousepad" to R.drawable.mp001,
    "Poleras Personalizadas" to R.drawable.pp001,
    "Polerones Gamers Personalizados" to R.drawable.pg001
)

data class AddProductUiState(
    val code: String = "",
    val name: String = "",
    val price: String = "",
    val oldPrice: String = "",
    val stock: String = "",
    val description: String = "",
    val category: String? = defaultCategories.firstOrNull(),
    val categories: List<String> = defaultCategories,
    val imageInputMode: AddProductImageMode = AddProductImageMode.NONE,
    val imageUrl: String = "",
    val imageUri: Uri? = null,
    val isSaving: Boolean = false,
    val codeError: String? = null,
    val nameError: String? = null,
    val priceError: String? = null,
    val stockError: String? = null,
    val imageError: String? = null,
    val categoryError: String? = null,
    val descriptionError: String? = null
)

enum class AddProductImageMode { NONE, GALLERY, URL }

sealed interface AddProductEvent {
    data class Success(val productName: String) : AddProductEvent
    data class Failure(val message: String) : AddProductEvent
}

class AddProductViewModel(
    private val createProductUseCase: CreateProductUseCase = CreateProductUseCase(ProductRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState: StateFlow<AddProductUiState> = _uiState

    private val _events = MutableSharedFlow<AddProductEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<AddProductEvent> = _events

    fun onCodeChange(value: String) {
        _uiState.update { current ->
            current.copy(code = value, codeError = null)
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, nameError = null) }
    }

    fun onPriceChange(value: String) {
        _uiState.update { it.copy(price = value, priceError = null) }
    }

    fun onOldPriceChange(value: String) {
        _uiState.update { it.copy(oldPrice = value) }
    }

    fun onStockChange(value: String) {
        _uiState.update { it.copy(stock = value, stockError = null) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value, descriptionError = null) }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(category = category, categoryError = null) }
    }

    fun onImageInputModeChange(mode: AddProductImageMode) {
        _uiState.update {
            it.copy(
                imageInputMode = mode,
                imageError = null,
                imageUri = if (mode == AddProductImageMode.GALLERY) it.imageUri else null,
                imageUrl = if (mode == AddProductImageMode.URL) it.imageUrl else ""
            )
        }
    }

    fun onImageUrlChange(value: String) {
        _uiState.update { it.copy(imageUrl = value, imageError = null) }
    }

    fun onImagePicked(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri, imageError = null) }
    }

    fun submitProduct() {
        val current = _uiState.value
        val trimmedCode = current.code.trim().uppercase()
        val trimmedName = current.name.trim()
        val normalizedPrice = current.price
            .replace(".", "")
            .replace(',', '.')
            .toDoubleOrNull()
        val normalizedOldPrice = current.oldPrice
            .takeIf { it.isNotBlank() }
            ?.replace(".", "")
            ?.replace(',', '.')
            ?.toDoubleOrNull()
        val stockValue = current.stock.trim().toIntOrNull()
        val selectedCategory = current.category
        val trimmedDescription = current.description.trim()
        val imageMode = current.imageInputMode
        val imageUrl = current.imageUrl.trim().takeIf { it.isNotBlank() }
        val imageUriString = current.imageUri?.toString()

        var hasError = false
        if (trimmedCode.isEmpty()) {
            hasError = true
            _uiState.update { it.copy(codeError = "Ingresa el código del producto") }
        }
        if (trimmedName.isEmpty()) {
            hasError = true
            _uiState.update { it.copy(nameError = "El nombre es obligatorio") }
        }
        if (normalizedPrice == null || normalizedPrice <= 0.0) {
            hasError = true
            _uiState.update { it.copy(priceError = "Ingresa un precio válido") }
        }
        if (stockValue == null || stockValue < 0) {
            hasError = true
            _uiState.update { it.copy(stockError = "Ingresa un stock válido") }
        }
        if (selectedCategory.isNullOrBlank()) {
            hasError = true
            _uiState.update { it.copy(categoryError = "Selecciona una categoría") }
        }
        if (trimmedDescription.isEmpty()) {
            hasError = true
            _uiState.update { it.copy(descriptionError = "Describe el producto brevemente") }
        }
        when (imageMode) {
            AddProductImageMode.NONE -> {
                hasError = true
                _uiState.update { it.copy(imageError = "Agrega una imagen para el producto") }
            }
            AddProductImageMode.GALLERY -> if (imageUriString.isNullOrBlank()) {
                hasError = true
                _uiState.update { it.copy(imageError = "Selecciona una imagen desde tu dispositivo") }
            }
            AddProductImageMode.URL -> {
                if (imageUrl.isNullOrBlank()) {
                    hasError = true
                    _uiState.update { it.copy(imageError = "Ingresa la URL de la imagen") }
                } else if (!Patterns.WEB_URL.matcher(imageUrl).matches()) {
                    hasError = true
                    _uiState.update { it.copy(imageError = "La URL de la imagen no es válida") }
                }
            }
        }
        if (hasError) return

        val safeCategory = selectedCategory!!
        val imageRes = categoryImageMap[safeCategory]
        val product = Product(
            id = 0,
            code = trimmedCode,
            name = trimmedName,
            price = normalizedPrice!!,
            oldPrice = normalizedOldPrice?.takeIf { it > 0.0 },
            rating = 0f,
            reviews = 0,
            imageRes = imageRes,
            imageUrl = if (imageMode == AddProductImageMode.URL) imageUrl else null,
            imageUri = if (imageMode == AddProductImageMode.GALLERY) imageUriString else null,
            category = safeCategory,
            description = trimmedDescription,
            stock = stockValue!!
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            runCatching { createProductUseCase(product) }
                .onSuccess { created ->
                    _uiState.update {
                        it.copy(
                            code = "",
                            name = "",
                            price = "",
                            oldPrice = "",
                            stock = "",
                            description = "",
                            imageInputMode = AddProductImageMode.NONE,
                            imageUrl = "",
                            imageUri = null,
                            isSaving = false,
                            codeError = null,
                            nameError = null,
                            priceError = null,
                            stockError = null,
                            imageError = null,
                            categoryError = null,
                            descriptionError = null,
                            category = safeCategory
                        )
                    }
                    _events.emit(AddProductEvent.Success(created.name))
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isSaving = false) }
                    _events.emit(AddProductEvent.Failure(error.message ?: "No pudimos crear el producto"))
                }
        }
    }
}
