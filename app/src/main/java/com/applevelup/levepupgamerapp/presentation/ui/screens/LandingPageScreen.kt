package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.presentation.ui.components.*
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.PureBlackBackground
import com.applevelup.levepupgamerapp.presentation.viewmodel.CartViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.LandingViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.statusBars

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPageScreen(
    navController: NavController,
    viewModel: LandingViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cartViewModel: CartViewModel = viewModel()
    val cartState by cartViewModel.uiState.collectAsState()
    val cartCount = cartState.items.sumOf { it.quantity }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isSearchVisible by rememberSaveable { mutableStateOf(false) }
    val isDrawerOpen = drawerState.isOpen
    val density = LocalDensity.current
    val statusBarPadding = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
    val drawerTopPadding = statusBarPadding + 64.dp

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onClose = { scope.launch { drawerState.close() } },
                onLogout = {
                    scope.launch {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                },
                topPadding = drawerTopPadding
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.2f),
        gesturesEnabled = true
    ) {
        Box {
            Scaffold(
                topBar = {
                    LandingPageTopBar(
                        navController = navController,
                        isDrawerOpen = isDrawerOpen,
                        onDrawerToggle = {
                            scope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                } else {
                                    drawerState.open()
                                }
                            }
                        },
                        isSearchVisible = isSearchVisible,
                        onSearchVisibilityChange = { isSearchVisible = it },
                        cartCount = cartCount,
                        onCartClick = { navController.navigate("carrito") }
                    )
                },
                containerColor = PureBlackBackground
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    item { FeaturedCarousel(state.promotions, state.currentPage) }
                    item { SectionTitle("Categorías Populares") }
                    item { CategoryChips(state.categories, navController) }
                    item { SectionTitle("Productos Destacados") }
                    item { SectionTitle("Nuevos Lanzamientos") }
                    item { ProductShowcaseRow(state.featured, navController) }
                    item { ProductShowcaseRow(state.newProducts, navController) }
                }
            }

            if (isSearchVisible) {
                val interactionSource = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 120.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.45f),
                                    Color.Black.copy(alpha = 0.75f)
                                )
                            )
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { isSearchVisible = false }
                )
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, onClose: () -> Unit, onLogout: () -> Unit, topPadding: Dp) {
    ModalDrawerSheet(
        modifier = Modifier
            .widthIn(max = 320.dp)
            .padding(top = topPadding),
        drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
        windowInsets = WindowInsets(0, 0, 0, 0),
        drawerTonalElevation = 8.dp,
        drawerContainerColor = Color(0xFF101014).copy(alpha = 0.95f),
        drawerContentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Menú", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

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
            Spacer(modifier = Modifier.weight(1f))
            DrawerItem("Cerrar Sesión", highlight = true) {
                onLogout()
                onClose()
            }
        }
    }
}

@Composable
fun DrawerItem(title: String, highlight: Boolean = false, onClick: () -> Unit) {
    val textColor = if (highlight) PrimaryPurple else Color.White
    val backgroundColor = if (highlight) PrimaryPurple.copy(alpha = 0.12f) else Color.Transparent
    Text(
        text = title,
        color = textColor,
        fontSize = 18.sp,
        fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Normal,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp)
    )
}
