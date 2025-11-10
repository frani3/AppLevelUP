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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController
) {
    var email by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var successMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // decorative circles
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
                text = "¿Olvidaste tu contraseña?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingresa tu correo y te enviaremos un enlace para restablecerla",
                fontSize = 14.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 3
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
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo Electrónico", color = Color.LightGray) },
                        placeholder = { Text("tucorreo@ejemplo.com", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = Color.LightGray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { /* enviar */ }),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    successMessage?.let { msg ->
                        Text(text = msg, color = Color(0xFF7CFC00), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Button(
                        onClick = {
                            if (email.isNotBlank()) {
                                isLoading = true
                            } else {
                                successMessage = null
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Enviar enlace", fontSize = 16.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text("¿Recordaste tu contraseña? ", color = Color.LightGray)
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

    // Side-effect: simulate sending email
    if (isLoading) {
        LaunchedEffect(Unit) {
            delay(1200)
            isLoading = false
            successMessage = "Enlace enviado. Revisa tu correo."
        }
    }
}
