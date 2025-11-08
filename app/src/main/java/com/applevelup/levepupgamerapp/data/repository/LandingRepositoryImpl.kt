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
        ProductSummary("Teclado Mecánico RGB", "$99.990", R.drawable.teclado_product),
        ProductSummary("Mouse Gamer Inalámbrico", "$64.990", R.drawable.mouse_product),
        ProductSummary("Silla Gamer Ergonómica", "$199.990", R.drawable.silla_product),
        ProductSummary("Notebook Gamer Asus", "$249.990", R.drawable.pc_product)
    )

    override fun getNewProducts() = listOf(
        ProductSummary("Headset 7.1 Surround", "$89.990", R.drawable.audifonos_product),
        ProductSummary("Mousepad XXL", "$29.990", R.drawable.mousepad_product),
        ProductSummary("Polerón LevelUp", "$149.990", R.drawable.poleron_product),
        ProductSummary("Micrófono Condensador", "$119.990", R.drawable.polera_product)
    )
}
