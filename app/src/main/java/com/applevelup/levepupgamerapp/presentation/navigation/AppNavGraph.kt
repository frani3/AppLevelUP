package com.applevelup.levepupgamerapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.applevelup.levepupgamerapp.presentation.ui.screens.*
import androidx.compose.animation.ExperimentalAnimationApi

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.Splash.route,
        modifier = modifier
    ) {
        // Auth
        composable(
            route = Destinations.Splash.route,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 400)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 280)) }
        ) { SplashScreen(navController) }
        composable(
            route = Destinations.Login.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { LoginScreen(navController) }
        composable(
            route = Destinations.Register.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { RegistroScreen(navController) }
        composable(
            route = Destinations.ForgotPassword.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { ForgotPasswordScreen(navController) }

        // Home / catálogo
        composable(
            route = Destinations.Landing.route,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 320)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 220)) }
        ) { LandingPageScreen(navController) }
        composable(
            route = Destinations.Catalog.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { CatalogScreen(navController) }
        composable(
            route = Destinations.Categories.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { CategoryScreen(navController) }
        composable(
            route = "${Destinations.Search.route}?query={query}",
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            }),
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { backStack ->
            val initialQuery = backStack.arguments?.getString("query").orEmpty()
            SearchScreen(navController, initialQuery)
        }

        composable(
            route = Destinations.ProductList.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { backStack ->
            val category = backStack.arguments?.getString("categoryName") ?: "Categoría"
            ProductListScreen(navController, category)
        }

        composable(
            route = Destinations.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType }),
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { backStack ->
            val id = backStack.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(navController, id)
        }

        composable(
            route = Destinations.AddProduct.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) {
            AddProductScreen(navController)
        }

        composable(
            route = Destinations.Favorites.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) {
            FavoritesScreen(navController)
        }

        // Usuario
        composable(
            route = Destinations.Profile.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { ProfileScreen(navController) }
        composable(
            route = Destinations.Account.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { AccountScreen(navController) }
        composable(
            route = Destinations.Notifications.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { NotificationsScreen(navController) }
        composable(
            route = Destinations.NotificationSettings.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { NotificationSettingsScreen(navController) }
        composable(
            route = Destinations.EditProfile.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { EditProfileScreen(navController) }

        // Carrito
        composable(
            route = Destinations.Cart.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { CartScreen(navController) }
        composable(
            route = Destinations.Checkout.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { CheckoutScreen(navController) }

        composable(
            route = Destinations.OrderSuccess.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("total") { type = NavType.IntType }
            ),
            enterTransition = { modalEnter() },
            exitTransition = { modalExit() },
            popEnterTransition = { modalEnter(reverse = true) },
            popExitTransition = { modalExit(reverse = true) }
        ) { backStack ->
            val orderId = backStack.arguments?.getString("orderId").orEmpty()
            val total = backStack.arguments?.getInt("total") ?: 0
            OrderSuccessScreen(navController, orderId, total)
        }

        // Pagos
        composable(
            route = Destinations.PaymentMethods.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { PaymentMethodsScreen(navController) }
        composable(
            route = Destinations.AddPaymentMethod.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { AddPaymentMethodScreen(navController) }

        // Direcciones
        composable(
            route = Destinations.Addresses.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { AddressScreen(navController) }
        composable(
            route = Destinations.AddAddress.route,
            enterTransition = { forwardEnter() },
            exitTransition = { forwardExit() },
            popEnterTransition = { backwardEnter() },
            popExitTransition = { backwardExit() }
        ) { AddAddressScreen(navController) }
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.forwardEnter(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(durationMillis = 200, delayMillis = 60))

private fun AnimatedContentTransitionScope<NavBackStackEntry>.forwardExit(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(durationMillis = 200))

private fun AnimatedContentTransitionScope<NavBackStackEntry>.backwardEnter(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(durationMillis = 200, delayMillis = 40))

private fun AnimatedContentTransitionScope<NavBackStackEntry>.backwardExit(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(durationMillis = 180))

private fun AnimatedContentTransitionScope<NavBackStackEntry>.modalEnter(reverse: Boolean = false): EnterTransition {
    val direction = if (reverse) 1 else -1
    return slideInVertically(
        animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing)
    ) { fullHeight -> direction * (fullHeight / 3) } +
        fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 30))
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.modalExit(reverse: Boolean = false): ExitTransition {
    val direction = if (reverse) 1 else -1
    return slideOutVertically(
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    ) { fullHeight -> direction * (fullHeight / 3) } +
        fadeOut(animationSpec = tween(durationMillis = 180))
}
