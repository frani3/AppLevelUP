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
            // Nuevas categorías
            "ssd" in lowercase || "disco" in lowercase || "almacenamiento" in lowercase -> Icons.Default.Storage
            "teclado" in lowercase || "keyboard" in lowercase -> Icons.Default.Keyboard
            "monitor" in lowercase || "pantalla" in lowercase -> Icons.Default.Monitor
            "auricular" in lowercase || "audífono" in lowercase || "headset" in lowercase -> Icons.Default.Headphones
            "cable" in lowercase || "adaptador" in lowercase -> Icons.Default.Cable
            "memoria" in lowercase || "ram" in lowercase -> Icons.Default.Memory
            "gpu" in lowercase || "tarjeta" in lowercase || "gráfica" in lowercase -> Icons.Default.DeveloperBoard
            "fuente" in lowercase || "power" in lowercase -> Icons.Default.Power
            "cooler" in lowercase || "ventilador" in lowercase || "enfriamiento" in lowercase -> Icons.Default.AcUnit
            "gabinete" in lowercase || "case" in lowercase || "torre" in lowercase -> Icons.Default.Computer
            "streaming" in lowercase || "cámara" in lowercase || "webcam" in lowercase -> Icons.Default.Videocam
            "micrófono" in lowercase || "micro" in lowercase -> Icons.Default.Mic
            "control" in lowercase || "joystick" in lowercase || "mando" in lowercase -> Icons.Default.SportsEsports
            "vr" in lowercase || "realidad virtual" in lowercase -> Icons.Default.Vrpano
            "luz" in lowercase || "led" in lowercase || "rgb" in lowercase -> Icons.Default.LightMode
            // Icono genérico para categorías no mapeadas
            else -> Icons.Default.NewReleases
        }
    }
}
