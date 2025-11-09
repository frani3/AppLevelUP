package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.viewmodel.LandingViewModel
import kotlinx.coroutines.launch
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPageScreen(
    navController: NavController,
    viewModel: LandingViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                LandingPageTopBar(
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = PureBlackBackground
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                item { SearchBar() }
                item { FeaturedCarousel(state.promotions, state.currentPage) }
                item { SectionTitle("Categorías Populares") }
                item { CategoryChips(state.categories) }
                item { SectionTitle("Productos Destacados") }
                item { SectionTitle("Nuevos Lanzamientos") }
                item { ProductShowcaseRow(state.featured, navController) }
                item { ProductShowcaseRow(state.newProducts, navController) }

            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
    ) {
        Text("Menú", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        DrawerItem("Perfil") {
            navController.navigate("perfil")
            onClose()
        }
        DrawerItem("Carrito") {
            navController.navigate("carrito")
            onClose()
        }
        DrawerItem("Métodos de Pago") {
            navController.navigate("metodos_pago")
            onClose()
        }
        DrawerItem("Notificaciones") {
            navController.navigate("notificaciones")
            onClose()
        }
        DrawerItem("Cerrar Sesión") {
            navController.navigate("login")
            onClose()
        }
    }
}

@Composable
fun DrawerItem(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    )
}
