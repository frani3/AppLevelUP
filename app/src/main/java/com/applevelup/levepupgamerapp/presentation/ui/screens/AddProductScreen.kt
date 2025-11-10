package com.applevelup.levepupgamerapp.presentation.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.ui.theme.TopBarAndDrawerColor
import com.applevelup.levepupgamerapp.presentation.viewmodel.AddProductEvent
import com.applevelup.levepupgamerapp.presentation.viewmodel.AddProductImageMode
import com.applevelup.levepupgamerapp.presentation.viewmodel.AddProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: AddProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var categoryExpanded by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        viewModel.onImagePicked(uri)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is AddProductEvent.Success -> snackbarHostState.showSnackbar("Producto \"${event.productName}\" agregado correctamente")
                is AddProductEvent.Failure -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        disabledTextColor = Color.LightGray,
        errorTextColor = Color(0xFFFFB4AB),
        focusedBorderColor = PrimaryPurple,
        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
        cursorColor = PrimaryPurple,
        errorCursorColor = Color(0xFFFFB4AB),
        focusedLabelColor = PrimaryPurple,
        unfocusedLabelColor = Color.LightGray,
        errorLabelColor = Color(0xFFFFB4AB)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Agregar producto",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                }
            )
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Completa los datos para publicar un nuevo producto.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = uiState.code,
                onValueChange = viewModel::onCodeChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Código del producto") },
                isError = uiState.codeError != null,
                colors = textFieldColors,
                singleLine = true,
                supportingText = {
                    uiState.codeError?.let { Text(it, color = Color(0xFFFFB4AB)) }
                }
            )

            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre") },
                isError = uiState.nameError != null,
                colors = textFieldColors,
                singleLine = true,
                supportingText = {
                    uiState.nameError?.let { Text(it, color = Color(0xFFFFB4AB)) }
                }
            )

            OutlinedTextField(
                value = uiState.price,
                onValueChange = viewModel::onPriceChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Precio (CLP)") },
                isError = uiState.priceError != null,
                colors = textFieldColors,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    uiState.priceError?.let { Text(it, color = Color(0xFFFFB4AB)) }
                }
            )

            OutlinedTextField(
                value = uiState.oldPrice,
                onValueChange = viewModel::onOldPriceChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Precio anterior (opcional)") },
                colors = textFieldColors,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = uiState.stock,
                onValueChange = viewModel::onStockChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Cantidad disponible") },
                isError = uiState.stockError != null,
                colors = textFieldColors,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    uiState.stockError?.let { Text(it, color = Color(0xFFFFB4AB)) }
                }
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                label = { Text("Descripción") },
                isError = uiState.descriptionError != null,
                colors = textFieldColors,
                supportingText = {
                    uiState.descriptionError?.let { Text(it, color = Color(0xFFFFB4AB)) }
                }
            )

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                OutlinedTextField(
                    value = uiState.category.orEmpty(),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("Categoría") },
                    isError = uiState.categoryError != null,
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                    },
                    colors = textFieldColors
                )
                androidx.compose.material3.DropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    uiState.categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                viewModel.onCategorySelected(category)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }
            uiState.categoryError?.let {
                Text(it, color = Color(0xFFFFB4AB), style = MaterialTheme.typography.labelSmall)
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Imagen del producto",
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val isGallerySelected = uiState.imageInputMode == AddProductImageMode.GALLERY
                    FilterChip(
                        selected = isGallerySelected,
                        onClick = {
                            val nextMode = if (isGallerySelected) AddProductImageMode.NONE else AddProductImageMode.GALLERY
                            viewModel.onImageInputModeChange(nextMode)
                        },
                        label = { Text("Desde dispositivo") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = if (isGallerySelected) Color.White else Color.LightGray
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.DarkGray,
                            selectedContainerColor = PrimaryPurple,
                            labelColor = Color.White,
                            selectedLabelColor = Color.White
                        )
                    )

                    val isUrlSelected = uiState.imageInputMode == AddProductImageMode.URL
                    FilterChip(
                        selected = isUrlSelected,
                        onClick = {
                            val nextMode = if (isUrlSelected) AddProductImageMode.NONE else AddProductImageMode.URL
                            viewModel.onImageInputModeChange(nextMode)
                        },
                        label = { Text("Desde URL") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = if (isUrlSelected) Color.White else Color.LightGray
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.DarkGray,
                            selectedContainerColor = PrimaryPurple,
                            labelColor = Color.White,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                if (uiState.imageInputMode == AddProductImageMode.NONE) {
                    Text(
                        text = "Selecciona una opción para cargar la imagen",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                when (uiState.imageInputMode) {
                    AddProductImageMode.GALLERY -> {
                        Button(
                            onClick = {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) {
                            Text("Seleccionar imagen", color = Color.White)
                        }
                        uiState.imageUri?.let { uri ->
                            ProductPreviewImage(
                                uri = uri,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )
                        }
                    }
                    AddProductImageMode.URL -> {
                        OutlinedTextField(
                            value = uiState.imageUrl,
                            onValueChange = viewModel::onImageUrlChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("URL de la imagen") },
                            colors = textFieldColors,
                            singleLine = true
                        )
                        if (uiState.imageUrl.isNotBlank()) {
                            ProductPreviewImage(
                                imageUrl = uiState.imageUrl.trim(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )
                        }
                    }
                    AddProductImageMode.NONE -> Unit
                }

                uiState.imageError?.let {
                    Text(it, color = Color(0xFFFFB4AB), style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { viewModel.submitProduct() },
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Guardando...", color = Color.White)
                } else {
                    Text("Guardar producto", color = Color.White, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProductPreviewImage(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    uri: Uri? = null
) {
    val model = when {
        uri != null -> uri
        !imageUrl.isNullOrBlank() -> imageUrl
        else -> null
    }

    if (model != null) {
        AsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(LocalContext.current)
                .data(model)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
