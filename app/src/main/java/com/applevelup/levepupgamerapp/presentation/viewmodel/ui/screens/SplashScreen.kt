package com.applevelup.levepupgamerapp.presentation.viewmodel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // Esta lógica de 2 segundos y navegación se mantiene igual
    LaunchedEffect(key1 = true) {
        delay(2000L)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

// --- DISEÑO NUEVO ---
    // Un Box que ocupa toda la pantalla, con fondo negro
    // y alinea su contenido al centro.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Fondo negro
        contentAlignment = Alignment.Center // Contenido centrado
    ) {
        // Tu imagen de logo (asegúrate que se llame así en drawable)
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "Logo LevelUp Gamer"
            // Nota: No usamos fillMaxSize() ni ContentScale.Crop aquí
            // para que el logo mantenga su tamaño original.
        )
    }
}