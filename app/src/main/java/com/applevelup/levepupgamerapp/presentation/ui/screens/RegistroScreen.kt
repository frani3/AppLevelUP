package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.data.ChileanRegionsData
import com.applevelup.levepupgamerapp.presentation.ui.theme.*
import com.applevelup.levepupgamerapp.presentation.viewmodel.RegistroViewModel
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: RegistroViewModel = viewModel ()
) {
    val state by viewModel.uiState.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // Birth dates allowed between 18 and 120 years ago.
    val latestAllowedBirthDateMillis = remember {
        Calendar.getInstance().apply { add(Calendar.YEAR, -18) }.timeInMillis
    }
    val earliestAllowedBirthDateMillis = remember {
        Calendar.getInstance().apply { add(Calendar.YEAR, -120) }.timeInMillis
    }
    val parsedBirthDateMillis = remember(state.birthDate) {
        state.birthDate.takeIf { it.isNotBlank() }?.let { rawDate ->
            runCatching { dateFormatter.parse(rawDate)?.time }.getOrNull()
        }
    }

    var regionExpanded by rememberSaveable { mutableStateOf(false) }
    var comunaExpanded by rememberSaveable { mutableStateOf(false) }
    val regionNames = remember { ChileanRegionsData.regions.map { it.name } }
    val regionMap = remember { ChileanRegionsData.regionToCommunes }
    val availableCommunes = remember(state.region) {
        regionMap[state.region].orEmpty()
    }

    LaunchedEffect(state.isRegisterSuccessful) {
        if (state.isRegisterSuccessful) {
            navController.navigate("landing_page") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(x = (-40).dp, y = (-40).dp)
                .background(PrimaryPurple.copy(alpha = 0.12f), shape = RoundedCornerShape(80.dp))
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = 40.dp, y = 40.dp)
                .background(PrimaryPurple.copy(alpha = 0.08f), shape = RoundedCornerShape(80.dp))
                .align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Cuenta",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingresa tus datos para crear tu cuenta",
                fontSize = 14.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C0C0D)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Nombre
                    OutlinedTextField(
                        value = state.firstName,
                        onValueChange = viewModel::onFirstNameChange,
                        label = { Text("Nombre", color = Color.LightGray) },
                        placeholder = { Text("Tu nombre", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryPurple,
                            unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                    state.errors.firstNameError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Apellidos
                    OutlinedTextField(
                        value = state.lastName,
                        onValueChange = viewModel::onLastNameChange,
                        label = { Text("Apellidos", color = Color.LightGray) },
                        placeholder = { Text("Tus apellidos", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryPurple,
                            unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                    state.errors.lastNameError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Correo
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text("Correo Electrónico", color = Color.LightGray) },
                        placeholder = { Text("superadmin@levelup.cl", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = Color.LightGray) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { /* foco siguiente */ }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryPurple,
                            unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                    state.errors.emailError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (state.qualifiesForDuocDiscount) {
                            "¡Correo DUOC detectado! Obtendrás 20% de descuento vitalicio en tus compras."
                        } else {
                            "Los correos @duoc.cl y @profesor.duoc.cl reciben 20% de descuento vitalicio."
                        },
                        color = if (state.qualifiesForDuocDiscount) PrimaryPurple else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = if (state.qualifiesForDuocDiscount) FontWeight.SemiBold else FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // RUN
                    OutlinedTextField(
                        value = state.run,
                        onValueChange = viewModel::onRunChange,
                        label = { Text("RUN", color = Color.LightGray) },
                        placeholder = { Text("12345678-9", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Characters
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryPurple,
                            unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                    state.errors.runError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fecha de nacimiento
                    val birthDateInteractionSource = remember { MutableInteractionSource() }
                    OutlinedTextField(
                        value = state.birthDate,
                        onValueChange = {},
                        label = { Text("Fecha de nacimiento", color = Color.LightGray) },
                        placeholder = { Text("DD/MM/AAAA", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = birthDateInteractionSource,
                                indication = null
                            ) { showDatePicker = true },
                        shape = RoundedCornerShape(12.dp),
                        readOnly = true,
                        supportingText = {
                            Text(
                                text = "Debes tener entre 18 y 120 años",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarMonth,
                                    contentDescription = "Seleccionar fecha",
                                    tint = Color.LightGray
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryPurple,
                            unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                    state.errors.birthDateError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    if (showDatePicker) {
                        val initialSelection = (parsedBirthDateMillis
                            ?.coerceIn(earliestAllowedBirthDateMillis, latestAllowedBirthDateMillis)
                            ?: latestAllowedBirthDateMillis)
                        val selectableBirthDates = remember {
                            object : SelectableDates {
                                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                    return utcTimeMillis in earliestAllowedBirthDateMillis..latestAllowedBirthDateMillis
                                }

                                override fun isSelectableYear(year: Int): Boolean {
                                    val calendar = Calendar.getInstance()
                                    val minYearCalendar = (calendar.clone() as Calendar).apply {
                                        timeInMillis = earliestAllowedBirthDateMillis
                                    }
                                    val maxYearCalendar = (calendar.clone() as Calendar).apply {
                                        timeInMillis = latestAllowedBirthDateMillis
                                    }
                                    return year in minYearCalendar.get(Calendar.YEAR)..maxYearCalendar.get(Calendar.YEAR)
                                }
                            }
                        }

                        val datePickerState = rememberDatePickerState(
                            initialSelectedDateMillis = initialSelection,
                            initialDisplayedMonthMillis = initialSelection,
                            selectableDates = selectableBirthDates
                        )

                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        val selectedMillis = datePickerState.selectedDateMillis
                                        if (selectedMillis != null) {
                                            val formattedDate = dateFormatter.format(Date(selectedMillis))
                                            viewModel.onBirthDateChange(formattedDate)
                                        }
                                        showDatePicker = false
                                    }
                                ) {
                                    Text("Aceptar")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) {
                                    Text("Cancelar")
                                }
                            }
                        ) {
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = false
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Código de referido (opcional)
                    OutlinedTextField(
                        value = state.referralCode,
                        onValueChange = viewModel::onReferralCodeChange,
                        label = { Text("Código de referido (opcional)", color = Color.LightGray) },
                        placeholder = { Text("LUG-XXXXX", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Characters
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryPurple,
                            unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Si alguien te invitó, ingresa su código para desbloquear puntos extra.",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Región y comuna
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = regionExpanded,
                            onExpandedChange = {
                                regionExpanded = !regionExpanded
                                if (!regionExpanded) {
                                    comunaExpanded = false
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = state.region,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Región", color = Color.LightGray) },
                                placeholder = { Text("Selecciona una región", color = Color.Gray) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = PrimaryPurple,
                                    unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                singleLine = true
                            )
                            ExposedDropdownMenu(
                                expanded = regionExpanded,
                                onDismissRequest = { regionExpanded = false }
                            ) {
                                regionNames.forEach { regionName ->
                                    DropdownMenuItem(
                                        text = { Text(regionName) },
                                        onClick = {
                                            regionExpanded = false
                                            comunaExpanded = false
                                            viewModel.onRegionChange(regionName)
                                        }
                                    )
                                }
                            }
                        }

                        ExposedDropdownMenuBox(
                            expanded = comunaExpanded,
                            onExpandedChange = {
                                if (availableCommunes.isNotEmpty()) {
                                    comunaExpanded = !comunaExpanded
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = state.comuna,
                                onValueChange = {},
                                readOnly = true,
                                enabled = availableCommunes.isNotEmpty(),
                                label = { Text("Comuna", color = Color.LightGray) },
                                placeholder = { Text("Selecciona una comuna", color = Color.Gray) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor(
                                        type = MenuAnchorType.PrimaryNotEditable,
                                        enabled = availableCommunes.isNotEmpty()
                                    )
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = PrimaryPurple,
                                    unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    disabledIndicatorColor = PrimaryPurple.copy(alpha = 0.3f),
                                    disabledTextColor = Color.LightGray
                                ),
                                singleLine = true
                            )
                            ExposedDropdownMenu(
                                expanded = comunaExpanded,
                                onDismissRequest = { comunaExpanded = false }
                            ) {
                                availableCommunes.forEach { comunaName ->
                                    DropdownMenuItem(
                                        text = { Text(comunaName) },
                                        onClick = {
                                            comunaExpanded = false
                                            viewModel.onComunaChange(comunaName)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    state.errors.regionError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }
                    state.errors.comunaError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dirección principal
                    OutlinedTextField(
                        value = state.address,
                        onValueChange = viewModel::onAddressChange,
                        label = { Text("Dirección principal", color = Color.LightGray) },
                        placeholder = { Text("Ej: Av. Siempre Viva 742, Springfield", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryPurple,
                            unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        minLines = 2,
                        maxLines = 3
                    )
                    state.errors.addressError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Contraseña
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text("Contraseña", color = Color.LightGray) },
                        placeholder = { Text("••••••••", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña", tint = Color.LightGray) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña", tint = Color.LightGray)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { /* foco siguiente */ }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryPurple,
                            unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                    state.errors.passwordError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Confirmar contraseña
                    OutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        label = { Text("Confirmar Contraseña", color = Color.LightGray) },
                        placeholder = { Text("••••••••", color = Color.Gray) },
                        leadingIcon = null,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = null, tint = Color.LightGray)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.register() }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryPurple,
                            unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                    state.errors.confirmPasswordError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Checkbox de Términos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = state.termsAccepted,
                            onCheckedChange = viewModel::onTermsChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = PrimaryPurple,
                                uncheckedColor = Color.LightGray
                            )
                        )
                        Text(text = "Acepto los términos y condiciones", color = Color.LightGray, fontSize = 14.sp)
                    }
                    state.errors.termsError?.let {
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 12.sp)
                    }

                    state.generalError?.let {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = it, color = Color(0xFFFF6B6B), fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón Registrarse
                    Button(
                        onClick = { viewModel.register() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Registrarse", fontSize = 16.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text("¿Ya tienes una cuenta? ", color = Color.LightGray)
                        Text(
                            "Inicia Sesión",
                            color = PrimaryPurple,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { navController.navigate("login") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = PrimaryPurple,
        unfocusedIndicatorColor = PrimaryPurple.copy(alpha = 0.5f),
        focusedLabelColor = Color.LightGray,
        unfocusedLabelColor = Color.LightGray,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedTrailingIconColor = Color.LightGray,
        unfocusedTrailingIconColor = Color.LightGray
    )
}
