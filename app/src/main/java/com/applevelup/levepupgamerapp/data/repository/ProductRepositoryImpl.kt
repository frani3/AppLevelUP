package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository

class ProductRepositoryImpl : ProductRepository {

    private val allProducts = mapOf(
        "Juegos de Mesa" to listOf(
            Product(101, "Catan - El Juego", 49.99, 59.99, 4.8f, 1345, R.drawable.catan_product),
            Product(102, "Carcassonne - Edición Plus", 39.99, null, 4.7f, 987, R.drawable.carcassonne_product)
        ),
        "Accesorios" to listOf(
            Product(201, "Headset 7.1 Surround", 89.99, null, 4.6f, 854, R.drawable.audifonos_product)
        ),
        "Consolas" to listOf(
            Product(301, "PlayStation 5", 549.99, null, 4.9f, 3012, R.drawable.p5_product)
        ),
        "Computadores Gamers" to listOf(
            Product(401, "Notebook Gamer Asus", 1249.99, 1499.99, 4.8f, 451, R.drawable.pc_product)
        ),
        "Sillas Gamers" to listOf(
            Product(501, "Silla Gamer Ergonómica", 199.99, null, 4.7f, 1123, R.drawable.silla_product)
        ),
        "Mouse" to listOf(
            Product(601, "Mouse Logitech G502 HERO", 64.99, 79.99, 4.9f, 2054, R.drawable.mouse_product)
        ),
        "Mousepad" to listOf(
            Product(701, "Mousepad XXL", 29.99, null, 4.8f, 1500, R.drawable.mousepad_product)
        ),
        "Poleras Personalizadas" to emptyList(),
        "Polerones Gamers Personalizados" to emptyList()
    )

    override fun getProductsByCategory(categoryName: String): List<Product> {
        return allProducts.entries.firstOrNull {
            it.key.equals(categoryName, ignoreCase = true)
        }?.value ?: emptyList()
    }
}
