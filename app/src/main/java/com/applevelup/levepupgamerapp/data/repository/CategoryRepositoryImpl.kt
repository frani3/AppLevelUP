package com.applevelup.levepupgamerapp.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.applevelup.levepupgamerapp.domain.model.CategoryInfo
import com.applevelup.levepupgamerapp.domain.repository.CategoryRepository

class CategoryRepositoryImpl : CategoryRepository {
    override fun getAllCategories() = listOf(
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
