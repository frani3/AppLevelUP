package com.applevelup.levepupgamerapp.presentation.navigation

sealed class Destinations(val route: String) {

    // Auth / sesión
    data object Splash : Destinations("splash")
    data object Login : Destinations("login")
    data object Register : Destinations("registro")

    // Home / catálogo
    data object Landing : Destinations("landing_page")
    data object Categories : Destinations("categorias")

    data object ProductList : Destinations("productos/{categoryName}") {
        fun create(categoryName: String) = "productos/$categoryName"
    }

    data object ProductDetail : Destinations("product_detail/{productId}") {
        fun create(productId: Int) = "product_detail/$productId"
    }

    // Usuario
    data object Profile : Destinations("perfil")
    data object Account : Destinations("gestionar_cuenta")
    data object Notifications : Destinations("notificaciones")

    // Carrito
    data object Cart : Destinations("carrito")

    // Pagos
    data object PaymentMethods : Destinations("metodos_pago")
    data object AddPaymentMethod : Destinations("agregar_metodo_pago")

    // Direcciones
    data object Addresses : Destinations("direcciones_envio")
    data object AddAddress : Destinations("agregar_direccion") // placeholder si luego la creas
}
