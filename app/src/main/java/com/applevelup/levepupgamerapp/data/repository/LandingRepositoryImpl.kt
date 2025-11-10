package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.domain.model.*
import com.applevelup.levepupgamerapp.domain.repository.LandingRepository

class LandingRepositoryImpl : LandingRepository {

    override fun getPromotions() = listOf(
        Promotion("OFERTAS EN JUEGOS", "Renueva tu ludoteca esta semana", R.drawable.promo_banner_1),
        Promotion("ACCESORIOS DESTACADOS", "Setup pro con envíos en 24h", R.drawable.promo_banner_2),
        Promotion("ESCRITORIO GAMER", "Sillas y PC listos para tu stream", R.drawable.promo_banner_2)
    )

    override fun getCategories() = listOf(
        Category("Juegos de Mesa", R.drawable.ic_keyboard),
        Category("Accesorios", R.drawable.ic_headset),
        Category("Consolas", R.drawable.ic_monitor),
        Category("Computadores Gamers", R.drawable.ic_monitor),
        Category("Sillas Gamers", R.drawable.ic_chair),
        Category("Mouse", R.drawable.ic_mouse)
    )

    override fun getFeaturedProducts() = listOf(
        ProductSummary(
            id = 1,
            name = "Catan",
            price = "$29.990",
            imageRes = R.drawable.jm001
        ),
        ProductSummary(
            id = 5,
            name = "HyperX Cloud II",
            price = "$79.990",
            imageRes = R.drawable.ac002
        ),
        ProductSummary(
            id = 7,
            name = "PlayStation 5",
            price = "$549.990",
            imageRes = R.drawable.co001
        ),
        ProductSummary(
            id = 9,
            name = "PC Gamer ASUS ROG",
            price = "$1.299.990",
            imageRes = R.drawable.cg001
        )
    )


    override fun getNewProducts() = listOf(
        ProductSummary(
            id = 11,
            name = "Secretlab Titan",
            price = "$349.990",
            imageRes = R.drawable.sg001
        ),
        ProductSummary(
            id = 13,
            name = "Logitech G502 HERO",
            price = "$49.990",
            imageRes = R.drawable.ms001
        ),
        ProductSummary(
            id = 19,
            name = "Polerón Respawn",
            price = "$24.990",
            imageRes = R.drawable.pg001
        )
    )

}
