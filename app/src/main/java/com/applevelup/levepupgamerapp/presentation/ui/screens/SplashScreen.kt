package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.presentation.viewmodel.SplashDestination
import com.applevelup.levepupgamerapp.presentation.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.destination) {
        when (state.destination) {
            SplashDestination.LOGIN -> {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
                viewModel.onNavigated()
            }
            SplashDestination.HOME -> {
                navController.navigate("landing_page") {
                    popUpTo("splash") { inclusive = true }
                }
                viewModel.onNavigated()
            }
            SplashDestination.NONE -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "Logo LevelUp Gamer"
        )
    }
}
