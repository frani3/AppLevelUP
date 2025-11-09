package com.applevelup.levepupgamerapp.data.local.seed

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.local.entity.ProductEntity
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity
import com.applevelup.levepupgamerapp.domain.model.ProductReview
import com.applevelup.levepupgamerapp.utils.SecurityUtils

object LocalSeedData {

    val defaultProducts = listOf(
        ProductEntity(
            id = 1,
            name = "Catan",
            price = 29990.0,
            oldPrice = 32990.0,
            rating = 4.8f,
            reviews = 256,
            imageRes = R.drawable.jm001,
            category = "Juegos de Mesa",
            description = "Catan es un clásico juego de estrategia donde los jugadores colonizan una isla, comercian recursos y compiten por la supremacía."
        ),
        ProductEntity(
            id = 2,
            name = "Carcassonne",
            price = 24990.0,
            oldPrice = 27990.0,
            rating = 4.7f,
            reviews = 198,
            imageRes = R.drawable.jm002,
            category = "Juegos de Mesa",
            description = "Carcassonne es un juego de colocación de losetas en el que construyes ciudades, caminos y monasterios para sumar puntos."
        ),
        ProductEntity(
            id = 3,
            name = "Dixit",
            price = 21990.0,
            oldPrice = 24990.0,
            rating = 4.6f,
            reviews = 174,
            imageRes = R.drawable.jm003,
            category = "Juegos de Mesa",
            description = "Dixit es un creativo juego de cartas ilustradas donde la imaginación y la interpretación son clave para ganar."
        ),
        ProductEntity(
            id = 4,
            name = "Control Xbox Series X",
            price = 59990.0,
            oldPrice = 64990.0,
            rating = 4.9f,
            reviews = 412,
            imageRes = R.drawable.ac001,
            category = "Accesorios",
            description = "Control inalámbrico original para Xbox Series X|S con ergonomía mejorada y respuesta precisa."
        ),
        ProductEntity(
            id = 5,
            name = "Auriculares HyperX Cloud II",
            price = 79990.0,
            oldPrice = 89990.0,
            rating = 4.8f,
            reviews = 368,
            imageRes = R.drawable.ac002,
            category = "Accesorios",
            description = "Headset HyperX Cloud II con sonido envolvente virtual 7.1 y micrófono con cancelación de ruido."
        ),
        ProductEntity(
            id = 6,
            name = "Teclado Mecánico Redragon Kumara",
            price = 39990.0,
            oldPrice = 44990.0,
            rating = 4.7f,
            reviews = 295,
            imageRes = R.drawable.ac003,
            category = "Accesorios",
            description = "Teclado mecánico compacto Redragon Kumara con switches durables e iluminación RGB."
        ),
        ProductEntity(
            id = 7,
            name = "PlayStation 5",
            price = 549990.0,
            oldPrice = null,
            rating = 4.9f,
            reviews = 512,
            imageRes = R.drawable.co001,
            category = "Consolas",
            description = "La consola PS5 ofrece gráficos de nueva generación, carga ultrarrápida con SSD y gatillos adaptativos."
        ),
        ProductEntity(
            id = 8,
            name = "Nintendo Switch OLED",
            price = 349990.0,
            oldPrice = 369990.0,
            rating = 4.8f,
            reviews = 421,
            imageRes = R.drawable.co002,
            category = "Consolas",
            description = "La consola Nintendo Switch OLED ofrece una pantalla OLED de 7 pulgadas, audio mejorado y un soporte ajustable para una experiencia de juego versátil tanto en modo portátil como en modo TV."
        ),
        ProductEntity(
            id = 9,
            name = "PC Gamer ASUS ROG Strix",
            price = 1299990.0,
            oldPrice = 1399990.0,
            rating = 4.8f,
            reviews = 215,
            imageRes = R.drawable.cg001,
            category = "Computadores Gamers",
            description = "PC gamer ASUS ROG Strix de alto rendimiento, ideal para juegos AAA y streaming."
        ),
        ProductEntity(
            id = 10,
            name = "Notebook Gamer MSI Katana",
            price = 999990.0,
            oldPrice = 1099990.0,
            rating = 4.7f,
            reviews = 189,
            imageRes = R.drawable.cg002,
            category = "Computadores Gamers",
            description = "Notebook MSI Katana con GPU dedicada y pantalla de alta tasa de refresco para gaming fluido."
        ),
        ProductEntity(
            id = 11,
            name = "Silla Gamer Secretlab Titan",
            price = 349990.0,
            oldPrice = 379990.0,
            rating = 4.8f,
            reviews = 164,
            imageRes = R.drawable.sg001,
            category = "Sillas Gamers",
            description = "Silla gamer Secretlab Titan con soporte ergonómico y materiales premium para sesiones largas."
        ),
        ProductEntity(
            id = 12,
            name = "Silla Gamer Cougar Armor One",
            price = 199990.0,
            oldPrice = 219990.0,
            rating = 4.6f,
            reviews = 132,
            imageRes = R.drawable.sg002,
            category = "Sillas Gamers",
            description = "Silla Cougar Armor One con estructura resistente y cojines ajustables para mayor comodidad."
        ),
        ProductEntity(
            id = 13,
            name = "Mouse Logitech G502 HERO",
            price = 49990.0,
            oldPrice = 54990.0,
            rating = 4.9f,
            reviews = 542,
            imageRes = R.drawable.ms001,
            category = "Mouse",
            description = "Mouse Logitech G502 HERO con sensor de alta precisión y pesos ajustables."
        ),
        ProductEntity(
            id = 14,
            name = "Mouse Razer DeathAdder V2",
            price = 39990.0,
            oldPrice = 44990.0,
            rating = 4.8f,
            reviews = 487,
            imageRes = R.drawable.ms002,
            category = "Mouse",
            description = "Mouse Razer DeathAdder V2 con diseño ergonómico y switches ópticos Razer."
        ),
        ProductEntity(
            id = 15,
            name = "Mousepad Razer Goliathus Extended Chroma",
            price = 29990.0,
            oldPrice = 32990.0,
            rating = 4.7f,
            reviews = 268,
            imageRes = R.drawable.mp001,
            category = "Mousepad",
            description = "Mousepad extendido con iluminación RGB Chroma y superficie optimizada para precisión."
        ),
        ProductEntity(
            id = 16,
            name = "Mousepad Logitech G Powerplay",
            price = 99990.0,
            oldPrice = 109990.0,
            rating = 4.6f,
            reviews = 143,
            imageRes = R.drawable.mp002,
            category = "Mousepad",
            description = "Mousepad Logitech Powerplay con carga inalámbrica continua para mouse compatibles."
        ),
        ProductEntity(
            id = 17,
            name = "Polera Gamer Personalizada 'Level-Up'",
            price = 14990.0,
            oldPrice = 17990.0,
            rating = 4.5f,
            reviews = 92,
            imageRes = R.drawable.pp001,
            category = "Poleras Personalizadas",
            description = "Polera personalizada Level-Up con diseño gamer, tela suave y resistente."
        ),
        ProductEntity(
            id = 18,
            name = "Polera Retro Arcade",
            price = 15990.0,
            oldPrice = 18990.0,
            rating = 4.6f,
            reviews = 88,
            imageRes = R.drawable.pp002,
            category = "Poleras Personalizadas",
            description = "Polera temática retro arcade con estampado de alta calidad y ajuste cómodo."
        ),
        ProductEntity(
            id = 19,
            name = "Polerón Gamer Hoodie 'Respawn'",
            price = 24990.0,
            oldPrice = 27990.0,
            rating = 4.7f,
            reviews = 104,
            imageRes = R.drawable.pg001,
            category = "Polerones Gamers Personalizados",
            description = "Polerón con capucha estilo gamer, interior suave y estampado Respawn."
        ),
        ProductEntity(
            id = 20,
            name = "Polerón Level-Up Logo",
            price = 26990.0,
            oldPrice = 29990.0,
            rating = 4.6f,
            reviews = 97,
            imageRes = R.drawable.pg002,
            category = "Polerones Gamers Personalizados",
            description = "Polerón con logo Level-Up, ideal para el día a día con estilo gamer."
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

    val productReviews: Map<Int, List<ProductReview>> = mapOf(
        1 to listOf(
            ProductReview(
                id = "1_1",
                author = "Valentina R.",
                rating = 5f,
                comment = "Un clásico imperdible, siempre distinto cada vez que lo jugamos.",
                date = "03 nov 2025"
            ),
            ProductReview(
                id = "1_2",
                author = "Rodrigo C.",
                rating = 4.8f,
                comment = "Componentes de buena calidad y las expansiones lo hacen aún mejor.",
                date = "28 oct 2025"
            )
        ),
        4 to listOf(
            ProductReview(
                id = "4_1",
                author = "Carolina M.",
                rating = 4.9f,
                comment = "El control se siente muy sólido y la batería dura bastante.",
                date = "30 oct 2025"
            )
        ),
        5 to listOf(
            ProductReview(
                id = "5_1",
                author = "Jean P.",
                rating = 4.7f,
                comment = "Sonido envolvente real y micrófono claro, perfectos para raids.",
                date = "15 oct 2025"
            )
        ),
        7 to listOf(
            ProductReview(
                id = "7_1",
                author = "Andrea S.",
                rating = 5f,
                comment = "La velocidad de carga y los gatillos adaptativos son increíbles.",
                date = "01 nov 2025"
            )
        ),
        9 to listOf(
            ProductReview(
                id = "9_1",
                author = "Ian L.",
                rating = 4.8f,
                comment = "Corre todos mis juegos AAA en ultra sin problemas.",
                date = "22 oct 2025"
            )
        ),
        13 to listOf(
            ProductReview(
                id = "13_1",
                author = "Paula V.",
                rating = 4.9f,
                comment = "Sensor muy preciso, ideal para shooters competitivos.",
                date = "05 nov 2025"
            )
        ),
        19 to listOf(
            ProductReview(
                id = "19_1",
                author = "Martín F.",
                rating = 4.6f,
                comment = "Abriga harto y el estampado se mantiene después de varios lavados.",
                date = "12 sep 2025"
            )
        )
    )
}
