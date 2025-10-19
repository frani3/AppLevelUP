package com.applevelup.levepupgamerapp.presentation.viewmodel.ui.screens

// En SplashScreen.kt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // 1. Esto ejecuta un bloque de código (coroutine) solo una vez
    //    cuando la pantalla aparece.
    LaunchedEffect(key1 = true) {
        // 2. Espera 2 segundos (2000 milisegundos)
        delay(2000L)

        // 3. Navega a la pantalla de login
        navController.navigate("login") {
            // 4. (Importante) Borra el Splash del historial.
            //    Así el usuario no puede "volver" al Splash.
            popUpTo("splash") { inclusive = true }
        }
    }

    // Mientras esperas, muestras tu logo o un texto
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "LevelUp APP") // <-- (Aquí puedes poner tu logo)
    }
}