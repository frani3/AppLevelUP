package com.applevelup.levepupgamerapp.presentation.navigation

import android.net.Uri

sealed class Destinations(val route: String) {

    // Auth / sesión
    data object Splash : Destinations("splash")
    data object Login : Destinations("login")
    data object Register : Destinations("registro")
    data object ForgotPassword : Destinations("forgot_password")

    // Home / catálogo
    data object Landing : Destinations("landing_page")
    data object Categories : Destinations("categorias")
    data object Search : Destinations("buscar") {
        fun withQuery(query: String) = "${route}?query=${Uri.encode(query)}"
    }

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
    data object EditProfile : Destinations("editar_perfil")

    // Carrito
    data object Cart : Destinations("carrito")
    data object Checkout : Destinations("checkout")

    // Pagos
    data object PaymentMethods : Destinations("metodos_pago")
    data object AddPaymentMethod : Destinations("agregar_metodo_pago")

    // Órdenes
    data object OrderSuccess : Destinations("orden_exitosa/{orderId}/{total}") {
        fun create(orderId: String, total: Int): String {
            val encodedId = Uri.encode(orderId)
            return "orden_exitosa/$encodedId/$total"
        }
    }

    // Direcciones
    data object Addresses : Destinations("direcciones_envio")
    data object AddAddress : Destinations("agregar_direccion") // placeholder si luego la creas
}
