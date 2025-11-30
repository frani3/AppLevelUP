package com.applevelup.levepupgamerapp.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.data.network.dto.CategoryDto
import com.applevelup.levepupgamerapp.domain.model.CategoryInfo
import com.applevelup.levepupgamerapp.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val api: LevelUpMobileApi = LevelUpDependencyContainer.api
) : CategoryRepository {

    override suspend fun getAllCategories(): List<CategoryInfo> {
        return runCatching {
            api.getCategories().map(::mapDto)
        }.getOrElse { fallbackCategories() }
    }

    private fun mapDto(dto: CategoryDto): CategoryInfo {
        return CategoryInfo(
            name = dto.nombre,
            icon = iconFor(dto.nombre),
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

    private fun fallbackCategories(): List<CategoryInfo> = listOf(
        CategoryInfo("Juegos de Mesa", Icons.Default.Casino, listOf("Catan", "Carcassonne")),
        CategoryInfo("Accesorios", Icons.Default.Headset, listOf("Control Xbox Series X", "Auriculares HyperX Cloud II")),
        CategoryInfo("Consolas", Icons.Default.VideogameAsset, listOf("PlayStation 5", "Nintendo Switch OLED")),
        CategoryInfo("Computadores Gamers", Icons.Default.DesktopWindows, listOf("PC Gamer ASUS ROG Strix", "Notebook MSI Katana")),
        CategoryInfo("Sillas Gamers", Icons.Default.Chair, listOf("Silla Gamer Secretlab Titan", "Silla Gamer Cougar Armor One")),
        CategoryInfo("Mouse", Icons.Default.Mouse, listOf("Mouse Logitech G502 HERO", "Mouse Razer DeathAdder V2")),
        CategoryInfo("Mousepad", Icons.Default.SquareFoot, listOf("Mousepad Razer Goliathus", "Mousepad Logitech G Powerplay")),
        CategoryInfo("Poleras Personalizadas", Icons.Default.Checkroom, listOf("Polera Gamer Personalizada", "Polera Retro Arcade")),
        CategoryInfo("Polerones Gamers", Icons.Default.Checkroom, listOf("Polerón Gamer 'Respawn'", "Polerón Level-Up Logo"))
    )
}
