package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple

@Composable
fun LoginScreen(
    navController: NavController
    // Descomenta esto cuando tu ViewModel esté listo
    // viewModel: LoginViewModel = viewModel()
) {
    // --- ESTADOS ---
    // Aquí guardamos lo que el usuario escribe.
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    // --- COLORES ---
    // Definimos los colores del diseño
    val backgroundColor = Color.Black
    //val primaryColor = Color(0xFFC000FF) // Usaremos el del tema centralizado
    val textFieldBackgroundColor = Color.DarkGray.copy(alpha = 0.3f)
    val lightTextColor = Color.LightGray

    // --- DISEÑO (UI) ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 32.dp), // Padding a los lados
        contentAlignment = Alignment.Center // Centra la columna
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Título "Iniciar Sesión"
            Text(
                text = "Iniciar Sesión",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 2. Campo de Texto: Correo
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo Electrónico", color = lightTextColor) },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = textFieldBackgroundColor,
                    unfocusedContainerColor = textFieldBackgroundColor,
                    disabledContainerColor = textFieldBackgroundColor,
                    focusedIndicatorColor = PrimaryPurple, // Color del borde al seleccionar
                    unfocusedIndicatorColor = PrimaryPurple, // Color del borde
                    focusedLabelColor = lightTextColor,
                    unfocusedLabelColor = lightTextColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Campo de Texto: Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña", color = lightTextColor) },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = textFieldBackgroundColor,
                    unfocusedContainerColor = textFieldBackgroundColor,
                    disabledContainerColor = textFieldBackgroundColor,
                    focusedIndicatorColor = PrimaryPurple,
                    unfocusedIndicatorColor = PrimaryPurple,
                    focusedLabelColor = lightTextColor,
                    unfocusedLabelColor = lightTextColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                // Lógica para ocultar/mostrar contraseña
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                // Ícono del ojo
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description, tint = lightTextColor)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Fila de "Recuérdame" y "Olvidaste..."
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox "Recuérdame"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryPurple,
                            uncheckedColor = lightTextColor
                        )
                    )
                    Text(text = "Recuérdame", color = lightTextColor, fontSize = 14.sp)
                }

                // Texto "Olvidaste..."
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = lightTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        // TODO: Lógica para recuperar contraseña
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 5. Botón "Ingresar"
            Button(
                onClick = {
                    // TODO: Aquí va la lógica de login
                    // Llamarías a: viewModel.login(email, password)

                    // De momento, solo navegamos a la landing page
                    navController.navigate("landing_page") {
                        // Borra Login y Splash del historial
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple
                )
            ) {
                Text(text = "Ingresar", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 6. TEXTO PARA CREAR CUENTA (NUEVO) ---
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = lightTextColor)) {
                        append("¿No tienes una cuenta? ")
                    }
                    withStyle(style = SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.Bold)) {
                        append("Regístrate")
                    }
                },
                modifier = Modifier.clickable {
                    navController.navigate("registro")
                }
            )
        }
    }
}
