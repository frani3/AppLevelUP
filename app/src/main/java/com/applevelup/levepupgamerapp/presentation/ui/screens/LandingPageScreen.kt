package com.applevelup.levepupgamerapp.presentation.ui.screens


import com.applevelup.levepupgamerapp.presentation.ui.components.*


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.applevelup.levepupgamerapp.presentation.ui.components.CategoryChips
import com.applevelup.levepupgamerapp.presentation.ui.components.FeaturedCarousel
import com.applevelup.levepupgamerapp.presentation.ui.components.LandingPageTopBar
import com.applevelup.levepupgamerapp.presentation.ui.components.ProductShowcaseRow
import com.applevelup.levepupgamerapp.presentation.ui.theme.*
import com.applevelup.levepupgamerapp.presentation.viewmodel.LandingViewModel

@Composable
fun LandingPageScreen(
    navController: NavController,
    viewModel: LandingViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { LandingPageTopBar (navController, onMenuClick = { /* TODO */ }) },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item { SearchBar() }
            item { FeaturedCarousel(promotions = state.promotions, currentPage = state.currentPage) }
            item { SectionTitle("Categorías Populares") }
            item { CategoryChips(categories = state.categories) }
            item { SectionTitle("Productos Destacados") }
            item { ProductShowcaseRow(products = state.featured) }
            item { SectionTitle("Nuevos Lanzamientos") }
            item { ProductShowcaseRow(products = state.newProducts) }
        }
    }
}
