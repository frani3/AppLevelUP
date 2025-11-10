package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.applevelup.levepupgamerapp.presentation.ui.theme.*
import com.applevelup.levepupgamerapp.presentation.viewmodel.RegistroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: RegistroViewModel = viewModel ()
) {
    val state by viewModel.uiState.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

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
                        value = state.username,
                        onValueChange = viewModel::onUsernameChange,
                        label = { Text("Nombre de Usuario", color = Color.LightGray) },
                        placeholder = { Text("Tu nombre completo", color = Color.Gray) },
                        leadingIcon = null,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, capitalization = KeyboardCapitalization.Words),
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
                    state.errors.usernameError?.let {
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

                    // Fecha de nacimiento
                    OutlinedTextField(
                        value = state.birthDate,
                        onValueChange = viewModel::onBirthDateChange,
                        label = { Text("Fecha de nacimiento", color = Color.LightGray) },
                        placeholder = { Text("DD/MM/AAAA", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        supportingText = {
                            Text(
                                text = "Debes tener entre 18 y 120 años",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
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
