package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.domain.model.*
import com.applevelup.levepupgamerapp.domain.repository.LandingRepository

class LandingRepositoryImpl : LandingRepository {

    override fun getPromotions() = listOf(
        Promotion("OFERTAS DE OTOÑO", "Hasta 40% en periféricos", R.drawable.promo_banner_1),
        Promotion("NUEVOS MONITORES", "Descubre la resolución 4K", R.drawable.promo_banner_1),
        Promotion("SILLAS GAMER PRO", "Comodidad para largas sesiones", R.drawable.promo_banner_1)
    )

    override fun getCategories() = listOf(
        Category("Teclados", R.drawable.ic_keyboard),
        Category("Mouse", R.drawable.ic_mouse),
        Category("Sillas", R.drawable.ic_chair),
        Category("Monitores", R.drawable.ic_monitor),
        Category("Headsets", R.drawable.ic_headset)
    )

    override fun getFeaturedProducts() = listOf(
        ProductSummary(
            id = 1,
            name = "Teclado Mecánico RGB",
            price = "$99.990",
            imageRes = R.drawable.teclado_product
        ),
        ProductSummary(
            id = 2,
            name = "Mouse Gamer Inalámbrico",
            price = "$64.990",
            imageRes = R.drawable.mouse_product
        ),
        ProductSummary(
            id = 3,
            name = "Silla Gamer Ergonómica",
            price = "$199.990",
            imageRes = R.drawable.silla_product
        ),
        ProductSummary(
            id = 4,
            name = "Notebook Gamer Asus",
            price = "$249.990",
            imageRes = R.drawable.pc_product
        )
    )


    override fun getNewProducts() = listOf(
        ProductSummary(
            id = 5,
            name = "Headset 7.1 Surround",
            price = "$89.990",
            imageRes = R.drawable.audifonos_product
        ),
        ProductSummary(
            id = 6,
            name = "Mousepad XXL",
            price = "$29.990",
            imageRes = R.drawable.mousepad_product
        ),
        ProductSummary(
            id = 7,
            name = "Polerón LevelUp",
            price = "$14.990",
            imageRes = R.drawable.poleron_product
        )
    )

}
