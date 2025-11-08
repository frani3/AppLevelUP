package com.applevelup.levepupgamerapp.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryInfo(
    val name: String,
    val icon: ImageVector,
    val sampleProducts: List<String>
)
