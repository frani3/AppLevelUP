package com.applevelup.levepupgamerapp.data.local.seed

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.local.entity.ProductEntity
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity
import com.applevelup.levepupgamerapp.utils.SecurityUtils

object LocalSeedData {

    val defaultProducts = listOf(
        ProductEntity(
            id = 1,
            name = "Teclado Mecánico RGB",
            price = 99990.0,
            oldPrice = 119990.0,
            rating = 4.8f,
            reviews = 1345,
            imageRes = R.drawable.teclado_product,
            category = "Accesorios",
            description = "Teclado mecánico con switches rojos y retroiluminación RGB personalizable."
        ),
        ProductEntity(
            id = 2,
            name = "Mouse Gamer Inalámbrico",
            price = 64990.0,
            oldPrice = 79990.0,
            rating = 4.9f,
            reviews = 987,
            imageRes = R.drawable.mouse_product,
            category = "Accesorios",
            description = "Mouse inalámbrico de baja latencia con sensor óptico de 26K DPI y batería para 140 horas."
        ),
        ProductEntity(
            id = 3,
            name = "Silla Gamer Ergonómica",
            price = 199990.0,
            oldPrice = null,
            rating = 4.7f,
            reviews = 1123,
            imageRes = R.drawable.silla_product,
            category = "Sillas Gamers",
            description = "Silla con soporte lumbar ajustable y reposabrazos 4D, ideal para sesiones largas."
        ),
        ProductEntity(
            id = 4,
            name = "Notebook Gamer Asus",
            price = 1249990.0,
            oldPrice = 1499990.0,
            rating = 4.8f,
            reviews = 451,
            imageRes = R.drawable.pc_product,
            category = "Computadores Gamers",
            description = "Notebook con GPU RTX Serie 40, pantalla 165Hz y sistema de refrigeración inteligente."
        ),
        ProductEntity(
            id = 5,
            name = "Headset 7.1 Surround",
            price = 89990.0,
            oldPrice = null,
            rating = 4.6f,
            reviews = 854,
            imageRes = R.drawable.audifonos_product,
            category = "Accesorios",
            description = "Audífonos con sonido envolvente 7.1, micrófono con cancelación de ruido y almohadillas de espuma."
        ),
        ProductEntity(
            id = 6,
            name = "Mousepad XXL",
            price = 29990.0,
            oldPrice = null,
            rating = 4.8f,
            reviews = 1500,
            imageRes = R.drawable.mousepad_product,
            category = "Accesorios",
            description = "Superficie de microfibra optimizada para precisión y base de goma antideslizante."
        ),
        ProductEntity(
            id = 7,
            name = "Polerón LevelUp",
            price = 14990.0,
            oldPrice = 19990.0,
            rating = 4.5f,
            reviews = 245,
            imageRes = R.drawable.poleron_product,
            category = "Ropa Gamer",
            description = "Polerón de algodón con impresión LevelUp resistente y suave al tacto."
        )
    )

    val superAdmin = UserEntity(
        id = 1L,
        fullName = "SuperAdmin LevelUp",
        email = "superadmin@levelup.cl",
        passwordHash = SecurityUtils.hashPassword("UltraSeguro123!"),
        avatarRes = R.drawable.avatar_placeholder,
        orderCount = 0,
        wishlistCount = 0,
        couponCount = 0,
        isSuperAdmin = true
    )

    val defaultUser = UserEntity(
        id = 2L,
        fullName = "FranI3",
        email = "frani3@email.com",
        passwordHash = SecurityUtils.hashPassword("LevelUp123"),
        avatarRes = R.drawable.avatar_placeholder,
        orderCount = 12,
        wishlistCount = 8,
        couponCount = 3,
        isSuperAdmin = false
    )
}
