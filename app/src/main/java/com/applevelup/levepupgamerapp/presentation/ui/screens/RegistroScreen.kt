package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController) {

    // --- ESTADOS ---
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var termsAccepted by rememberSaveable { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlackBackground)
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                // 1. Título "Crear Cuenta"
                Text(
                    text = "Crear Cuenta",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(32.dp))

                // 2. Campos de Texto
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre de Usuario") },
                    colors = getTextFieldColors(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Correo Electrónico") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = getTextFieldColors(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contraseña") },
                    colors = getTextFieldColors(),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, "Toggle password visibility")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirmar Contraseña") },
                    colors = getTextFieldColors(),
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = image, "Toggle password visibility")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))

                // 3. Checkbox de Términos y Condiciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryPurple,
                            uncheckedColor = Color.LightGray
                        )
                    )
                    Text(text = "Acepto los términos y condiciones", color = Color.LightGray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(32.dp))

                // 4. Botón "Registrarse"
                Button(
                    onClick = {
                        // TODO: Lógica de registro (validaciones, etc.)
                        // viewModel.register(...)
                        navController.navigate("landing_page") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    enabled = termsAccepted // El botón solo se activa si se aceptan los términos
                ) {
                    Text(text = "Registrarse", fontSize = 18.sp, color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // 5. Texto para volver a Iniciar Sesión
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.LightGray)) {
                            append("¿Ya tienes una cuenta? ")
                        }
                        withStyle(style = SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.Bold)) {
                            append("Inicia Sesión")
                        }
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Copiamos la función de ayuda para los colores de los campos de texto
@Composable
private fun getTextFieldColors(): TextFieldColors {
    val textFieldBackgroundColor = Color.DarkGray.copy(alpha = 0.3f)
    val lightTextColor = Color.LightGray
    return TextFieldDefaults.colors(
        focusedContainerColor = textFieldBackgroundColor,
        unfocusedContainerColor = textFieldBackgroundColor,
        disabledContainerColor = textFieldBackgroundColor,
        focusedIndicatorColor = PrimaryPurple,
        unfocusedIndicatorColor = Color.Gray,
        focusedLabelColor = lightTextColor,
        unfocusedLabelColor = lightTextColor,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedTrailingIconColor = lightTextColor,
        unfocusedTrailingIconColor = lightTextColor
    )
}
