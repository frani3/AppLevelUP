// --- ESTA ES LA RUTA DE PAQUETE CORRECTA ---
package com.applevelup.levepupgamerapp.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography
import com.applevelup.levepupgamerapp.presentation.viewmodel.ui.theme.Typography

// 2. USAMOS NUESTROS COLORES PERSONALIZADOS PARA EL TEMA
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,       // Color principal para botones, bordes activos, etc.
    secondary = CardBackgroundColor, // Color secundario para elementos como chips.
    background = PureBlackBackground,  // Color de fondo principal de la app.
    surface = CardBackgroundColor,     // Color de superficie para tarjetas, menús, etc.
    onPrimary = Color.White,       // Color del texto sobre el color primario (ej. en un botón).
    onSecondary = Color.White,     // Color del texto sobre el color secundario.
    onBackground = Color.White,    // Color del texto sobre el color de fondo.
    onSurface = Color.White        // Color del texto sobre las superficies.
)

@Composable
fun LevepUpGamerAPPTheme(
    // Forzamos el tema oscuro para mantener la estética de la app
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    // Usamos siempre nuestro DarkColorScheme personalizado
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

