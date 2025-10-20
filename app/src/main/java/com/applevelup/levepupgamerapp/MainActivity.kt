package com.applevelup.levepupgamerapp

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.activity.enableEdgeToEdge
import com.applevelup.levepupgamerapp.presentation.ui.screens.AgregarEditarDireccionScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.AgregarMetodoPagoScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.CarritoScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.CategoriasScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.DireccionesEnvioScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.GestionarCuentaScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.LandingPageScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.LoginScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.MetodosPagoScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.NotificacionesScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.PerfilScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.ProductDetailScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.ProductListScreen
import com.applevelup.levepupgamerapp.presentation.ui.screens.RegistroScreen
import com.applevelup.levepupgamerapp.presentation.ui.theme.LevepUpGamerAPPTheme
import com.applevelup.levepupgamerapp.presentation.viewmodel.ui.screens.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LevepUpGamerAPPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Creamos el controlador de navegación
                    val navController = rememberNavController()

                    // 2. Creamos el "mapa" (NavHost)
                    NavHost(
                        navController = navController,
                        startDestination = "splash" // Define la pantalla inicial
                    ) {
                        // Aquí defines cada "ruta" y qué pantalla mostrar
                        composable(route = "splash") {
                            SplashScreen(navController = navController)
                        }

                        composable(route = "login") {
                            LoginScreen(navController = navController)
                        }

                        composable(route = "registro") {
                            RegistroScreen(navController = navController)
                        }

                        composable(route = "landing_page") {
                            LandingPageScreen(navController = navController)
                        }

                        composable(route = "categorias") {
                            CategoriasScreen(navController = navController)
                        }

                        composable(route = "perfil") {
                            PerfilScreen(navController = navController)
                        }

                        composable(route = "carrito") {
                            CarritoScreen(navController = navController)
                        }
                        composable(route = "gestionar_cuenta") { // <-- AÑADE ESTA NUEVA RUTA
                            GestionarCuentaScreen(navController = navController)
                        }
                        composable(route = "direcciones_envio") {
                            DireccionesEnvioScreen(navController = navController)
                        }
                        composable(route = "agregar_direccion") {
                            AgregarEditarDireccionScreen(navController = navController)
                        }
                        composable(route = "metodos_pago") {
                            MetodosPagoScreen(navController = navController)
                        }
                        composable(route = "agregar_metodo_pago") {
                            AgregarMetodoPagoScreen(navController = navController)
                        }
                        composable(route = "notificaciones") {
                            NotificacionesScreen(navController = navController)
                        }
                        composable(
                            route = "productos/{categoryName}"
                        ) { backStackEntry ->
                            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Categoría"
                            ProductListScreen(navController = navController, categoryName = categoryName)
                        }
                        composable(
                            route = "product_detail/{productId}",
                            arguments = listOf(navArgument("productId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                            ProductDetailScreen(navController = navController, productId = productId)
                        }
                    }
                }
            }
        }
    }
}