package com.applevelup.levepupgamerapp.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.model.CategoryInfo
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCategory
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first

class CategoryRepositoryImpl : CategoryRepository {

    private val levelUpCategories = LevelUpDependencyContainer.categoryRepository

    override suspend fun getAllCategories(): List<CategoryInfo> {
        val cached = levelUpCategories.observeCategories().first()
        if (cached.isNotEmpty()) return cached.map(::mapDomain)

        val refresh = levelUpCategories.refreshCategories(force = true)
        if (refresh is LevelUpResult.Failure) {
            return emptyList()
        }

        return levelUpCategories.observeCategories().first().map(::mapDomain)
    }

    private fun mapDomain(category: LevelUpCategory): CategoryInfo {
        return CategoryInfo(
            name = category.name,
            icon = iconFor(category.name),
            sampleProducts = emptyList()
        )
    }

    private fun iconFor(name: String): androidx.compose.ui.graphics.vector.ImageVector {
        val lowercase = name.lowercase()
        return when {
            "juego" in lowercase -> Icons.Default.Casino
            "accesorio" in lowercase -> Icons.Default.Headset
            "consol" in lowercase -> Icons.Default.VideogameAsset
            "computador" in lowercase || "pc" in lowercase -> Icons.Default.DesktopWindows
            "silla" in lowercase -> Icons.Default.Chair
            "mousepad" in lowercase -> Icons.Default.SquareFoot
            "mouse" in lowercase -> Icons.Default.Mouse
            "polera" in lowercase || "polerón" in lowercase -> Icons.Default.Checkroom
            else -> Icons.Default.Tag
        }
    }
}
