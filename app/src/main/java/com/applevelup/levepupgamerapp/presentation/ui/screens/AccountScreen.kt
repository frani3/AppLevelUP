package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.components.AccountForm
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.ui.theme.TopBarAndDrawerColor
import com.applevelup.levepupgamerapp.presentation.viewmodel.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gestionar Cuenta") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                    }
                }
            )
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { AccountForm(state, viewModel) }
        }
    }
}
