package com.applevelup.levepupgamerapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.applevelup.levepupgamerapp.presentation.ui.screens.*

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.Splash.route,
        modifier = modifier
    ) {
        // Auth
        composable(Destinations.Splash.route) { SplashScreen(navController) }
        composable(Destinations.Login.route) { LoginScreen(navController) }
        composable(Destinations.Register.route) { RegistroScreen(navController) }

        // Home / catálogo
        composable(Destinations.Landing.route) { LandingPageScreen(navController) }
        composable(Destinations.Categories.route) { CategoryScreen(navController) }
        composable(
            route = "${Destinations.Search.route}?query={query}",
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { backStack ->
            val initialQuery = backStack.arguments?.getString("query").orEmpty()
            SearchScreen(navController, initialQuery)
        }

        composable(Destinations.ProductList.route) { backStack ->
            val category = backStack.arguments?.getString("categoryName") ?: "Categoría"
            ProductListScreen(navController, category)
        }

        composable(
            route = Destinations.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(navController, id)
        }

        // Usuario
        composable(Destinations.Profile.route) { ProfileScreen(navController) }
        composable(Destinations.Account.route) { AccountScreen(navController) }
        composable(Destinations.Notifications.route) { NotificationsScreen(navController) }
    composable(Destinations.EditProfile.route) { EditProfileScreen(navController) }

        // Carrito
        composable(Destinations.Cart.route) { CartScreen(navController) }
        composable(Destinations.Checkout.route) { CheckoutScreen(navController) }

        composable(
            route = Destinations.OrderSuccess.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStack ->
            val orderId = backStack.arguments?.getString("orderId").orEmpty()
            val total = backStack.arguments?.getInt("total") ?: 0
            OrderSuccessScreen(navController, orderId, total)
        }

        // Pagos
        composable(Destinations.PaymentMethods.route) { PaymentMethodsScreen(navController) }
        composable(Destinations.AddPaymentMethod.route) { AddPaymentMethodScreen(navController) }

        // Direcciones
        composable(Destinations.Addresses.route) { AddressScreen(navController) }
        composable(Destinations.AddAddress.route) { AddAddressScreen(navController) }
    }
}
