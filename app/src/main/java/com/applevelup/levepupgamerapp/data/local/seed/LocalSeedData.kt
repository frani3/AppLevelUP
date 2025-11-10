package com.applevelup.levepupgamerapp.data.local.seed

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.local.entity.ProductEntity
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity
import com.applevelup.levepupgamerapp.domain.model.ProductReview

object LocalSeedData {

    val defaultProducts = listOf(
        ProductEntity(
            id = 1,
            code = "PRD-0001",
            name = "Catan",
            price = 29990.0,
            oldPrice = 32990.0,
            rating = 4.8f,
            reviews = 256,
            imageRes = R.drawable.jm001,
            imageUrl = null,
            imageUri = null,
            category = "Juegos de Mesa",
            description = "Catan es un clásico juego de estrategia donde los jugadores colonizan una isla, comercian recursos y compiten por la supremacía.",
            stock = 24
        ),
        ProductEntity(
            id = 2,
            code = "PRD-0002",
            name = "Carcassonne",
            price = 24990.0,
            oldPrice = 27990.0,
            rating = 4.7f,
            reviews = 198,
            imageRes = R.drawable.jm002,
            imageUrl = null,
            imageUri = null,
            category = "Juegos de Mesa",
            description = "Carcassonne es un juego de colocación de losetas en el que construyes ciudades, caminos y monasterios para sumar puntos.",
            stock = 18
        ),
        ProductEntity(
            id = 3,
            code = "PRD-0003",
            name = "Dixit",
            price = 21990.0,
            oldPrice = 24990.0,
            rating = 4.6f,
            reviews = 174,
            imageRes = R.drawable.jm003,
            imageUrl = null,
            imageUri = null,
            category = "Juegos de Mesa",
            description = "Dixit es un creativo juego de cartas ilustradas donde la imaginación y la interpretación son clave para ganar.",
            stock = 22
        ),
        ProductEntity(
            id = 4,
            code = "PRD-0004",
            name = "Control Xbox Series X",
            price = 59990.0,
            oldPrice = 64990.0,
            rating = 4.9f,
            reviews = 412,
            imageRes = R.drawable.ac001,
            imageUrl = null,
            imageUri = null,
            category = "Accesorios",
            description = "Control inalámbrico original para Xbox Series X|S con ergonomía mejorada y respuesta precisa.",
            stock = 30
        ),
        ProductEntity(
            id = 5,
            code = "PRD-0005",
            name = "Auriculares HyperX Cloud II",
            price = 79990.0,
            oldPrice = 89990.0,
            rating = 4.8f,
            reviews = 368,
            imageRes = R.drawable.ac002,
            imageUrl = null,
            imageUri = null,
            category = "Accesorios",
            description = "Headset HyperX Cloud II con sonido envolvente virtual 7.1 y micrófono con cancelación de ruido.",
            stock = 27
        ),
        ProductEntity(
            id = 6,
            code = "PRD-0006",
            name = "Teclado Mecánico Redragon Kumara",
            price = 39990.0,
            oldPrice = 44990.0,
            rating = 4.7f,
            reviews = 295,
            imageRes = R.drawable.ac003,
            imageUrl = null,
            imageUri = null,
            category = "Accesorios",
            description = "Teclado mecánico compacto Redragon Kumara con switches durables e iluminación RGB.",
            stock = 35
        ),
        ProductEntity(
            id = 7,
            code = "PRD-0007",
            name = "PlayStation 5",
            price = 549990.0,
            oldPrice = null,
            rating = 4.9f,
            reviews = 512,
            imageRes = R.drawable.co001,
            imageUrl = null,
            imageUri = null,
            category = "Consolas",
            description = "La consola PS5 ofrece gráficos de nueva generación, carga ultrarrápida con SSD y gatillos adaptativos.",
            stock = 12
        ),
        ProductEntity(
            id = 8,
            code = "PRD-0008",
            name = "Nintendo Switch OLED",
            price = 349990.0,
            oldPrice = 369990.0,
            rating = 4.8f,
            reviews = 421,
            imageRes = R.drawable.co002,
            imageUrl = null,
            imageUri = null,
            category = "Consolas",
            description = "La consola Nintendo Switch OLED ofrece una pantalla OLED de 7 pulgadas, audio mejorado y un soporte ajustable para una experiencia de juego versátil tanto en modo portátil como en modo TV.",
            stock = 19
        ),
        ProductEntity(
            id = 9,
            code = "PRD-0009",
            name = "PC Gamer ASUS ROG Strix",
            price = 1299990.0,
            oldPrice = 1399990.0,
            rating = 4.8f,
            reviews = 215,
            imageRes = R.drawable.cg001,
            imageUrl = null,
            imageUri = null,
            category = "Computadores Gamers",
            description = "PC gamer ASUS ROG Strix de alto rendimiento, ideal para juegos AAA y streaming.",
            stock = 8
        ),
        ProductEntity(
            id = 10,
            code = "PRD-0010",
            name = "Notebook Gamer MSI Katana",
            price = 999990.0,
            oldPrice = 1099990.0,
            rating = 4.7f,
            reviews = 189,
            imageRes = R.drawable.cg002,
            imageUrl = null,
            imageUri = null,
            category = "Computadores Gamers",
            description = "Notebook MSI Katana con GPU dedicada y pantalla de alta tasa de refresco para gaming fluido.",
            stock = 11
        ),
        ProductEntity(
            id = 11,
            code = "PRD-0011",
            name = "Silla Gamer Secretlab Titan",
            price = 349990.0,
            oldPrice = 379990.0,
            rating = 4.8f,
            reviews = 164,
            imageRes = R.drawable.sg001,
            imageUrl = null,
            imageUri = null,
            category = "Sillas Gamers",
            description = "Silla gamer Secretlab Titan con soporte ergonómico y materiales premium para sesiones largas.",
            stock = 16
        ),
        ProductEntity(
            id = 12,
            code = "PRD-0012",
            name = "Silla Gamer Cougar Armor One",
            price = 199990.0,
            oldPrice = 219990.0,
            rating = 4.6f,
            reviews = 132,
            imageRes = R.drawable.sg002,
            imageUrl = null,
            imageUri = null,
            category = "Sillas Gamers",
            description = "Silla Cougar Armor One con estructura resistente y cojines ajustables para mayor comodidad.",
            stock = 21
        ),
        ProductEntity(
            id = 13,
            code = "PRD-0013",
            name = "Mouse Logitech G502 HERO",
            price = 49990.0,
            oldPrice = 54990.0,
            rating = 4.9f,
            reviews = 542,
            imageRes = R.drawable.ms001,
            imageUrl = null,
            imageUri = null,
            category = "Mouse",
            description = "Mouse Logitech G502 HERO con sensor de alta precisión y pesos ajustables.",
            stock = 40
        ),
        ProductEntity(
            id = 14,
            code = "PRD-0014",
            name = "Mouse Razer DeathAdder V2",
            price = 39990.0,
            oldPrice = 44990.0,
            rating = 4.8f,
            reviews = 487,
            imageRes = R.drawable.ms002,
            imageUrl = null,
            imageUri = null,
            category = "Mouse",
            description = "Mouse Razer DeathAdder V2 con diseño ergonómico y switches ópticos Razer.",
            stock = 33
        ),
        ProductEntity(
            id = 15,
            code = "PRD-0015",
            name = "Mousepad Razer Goliathus Extended Chroma",
            price = 29990.0,
            oldPrice = 32990.0,
            rating = 4.7f,
            reviews = 268,
            imageRes = R.drawable.mp001,
            imageUrl = null,
            imageUri = null,
            category = "Mousepad",
            description = "Mousepad extendido con iluminación RGB Chroma y superficie optimizada para precisión.",
            stock = 29
        ),
        ProductEntity(
            id = 16,
            code = "PRD-0016",
            name = "Mousepad Logitech G Powerplay",
            price = 99990.0,
            oldPrice = 109990.0,
            rating = 4.6f,
            reviews = 143,
            imageRes = R.drawable.mp002,
            imageUrl = null,
            imageUri = null,
            category = "Mousepad",
            description = "Mousepad Logitech Powerplay con carga inalámbrica continua para mouse compatibles.",
            stock = 14
        ),
        ProductEntity(
            id = 17,
            code = "PRD-0017",
            name = "Polera Gamer Personalizada 'Level-Up'",
            price = 14990.0,
            oldPrice = 17990.0,
            rating = 4.5f,
            reviews = 92,
            imageRes = R.drawable.pp001,
            imageUrl = null,
            imageUri = null,
            category = "Poleras Personalizadas",
            description = "Polera personalizada Level-Up con diseño gamer, tela suave y resistente.",
            stock = 37
        ),
        ProductEntity(
            id = 18,
            code = "PRD-0018",
            name = "Polera Retro Arcade",
            price = 15990.0,
            oldPrice = 18990.0,
            rating = 4.6f,
            reviews = 88,
            imageRes = R.drawable.pp002,
            imageUrl = null,
            imageUri = null,
            category = "Poleras Personalizadas",
            description = "Polera temática retro arcade con estampado de alta calidad y ajuste cómodo.",
            stock = 41
        ),
        ProductEntity(
            id = 19,
            code = "PRD-0019",
            name = "Polerón Gamer Hoodie 'Respawn'",
            price = 24990.0,
            oldPrice = 27990.0,
            rating = 4.7f,
            reviews = 104,
            imageRes = R.drawable.pg001,
            imageUrl = null,
            imageUri = null,
            category = "Polerones Gamers Personalizados",
            description = "Polerón con capucha estilo gamer, interior suave y estampado Respawn.",
            stock = 28
        ),
        ProductEntity(
            id = 20,
            code = "PRD-0020",
            name = "Polerón Level-Up Logo",
            price = 26990.0,
            oldPrice = 29990.0,
            rating = 4.6f,
            reviews = 97,
            imageRes = R.drawable.pg002,
            imageUrl = null,
            imageUri = null,
            category = "Polerones Gamers Personalizados",
            description = "Polerón con logo Level-Up, ideal para el día a día con estilo gamer.",
            stock = 25
        )
    )

    private fun seedUser(
        id: Long,
        run: String,
        nombre: String,
        apellidos: String,
        correo: String,
        perfil: String,
        fechaNacimiento: String?,
        region: String,
        comuna: String,
        direccion: String,
        descuentoVitalicio: Boolean,
        referral: String? = null,
        isSystem: Boolean = false
    ): UserEntity {
        val cleanedFirstName = nombre.trim()
        val cleanedLastName = apellidos.trim()
        val fullName = listOf(cleanedFirstName, cleanedLastName)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { cleanedFirstName }
        val normalizedEmail = correo.trim().lowercase()
        val roleIsAdmin = perfil.equals("Administrador", ignoreCase = true)
        val placeholderAvatar = R.drawable.avatar_placeholder
        return UserEntity(
            id = id,
            run = run.trim().ifBlank { null },
            fullName = fullName,
            firstName = cleanedFirstName.ifBlank { null },
            lastName = cleanedLastName.ifBlank { null },
            email = normalizedEmail,
            passwordHash = null,
            avatarRes = placeholderAvatar,
            profileRole = perfil,
            birthDate = fechaNacimiento?.takeIf { it.isNotBlank() },
            region = region.trim().ifBlank { null },
            comuna = comuna.trim().ifBlank { null },
            address = direccion.trim().ifBlank { null },
            referralCode = referral?.trim().takeUnless { it.isNullOrBlank() },
            hasLifetimeDiscount = descuentoVitalicio,
            isSuperAdmin = isSystem || roleIsAdmin,
            isSystem = isSystem
        )
    }

    val seededUsers: List<UserEntity> = listOf(
        seedUser(
            id = 1,
            run = "000000000",
            nombre = "System",
            apellidos = "",
            correo = "system@levelup.local",
            perfil = "Administrador",
            fechaNacimiento = null,
            region = "",
            comuna = "",
            direccion = "",
            descuentoVitalicio = false,
            isSystem = true
        ),
        seedUser(
            id = 2,
            run = "190110222",
            nombre = "Felipe",
            apellidos = "Ahumada Silva",
            correo = "felipe@duoc.cl",
            perfil = "Cliente",
            fechaNacimiento = "1994-05-12",
            region = "Biobío",
            comuna = "Concepción",
            direccion = "Av. Los Carrera 123",
            descuentoVitalicio = true
        ),
        seedUser(
            id = 3,
            run = "123456785",
            nombre = "Ana",
            apellidos = "Pérez Gómez",
            correo = "ana@gmail.com",
            perfil = "Vendedor",
            fechaNacimiento = "1988-11-03",
            region = "RM",
            comuna = "Santiago",
            direccion = "Av. Providencia 456",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 4,
            run = "876543214",
            nombre = "Carlos",
            apellidos = "Muñoz Torres",
            correo = "carlos@profesor.duoc.cl",
            perfil = "Administrador",
            fechaNacimiento = "1982-07-21",
            region = "Valparaíso",
            comuna = "Viña del Mar",
            direccion = "Calle Marina 789",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 5,
            run = "111111111",
            nombre = "María",
            apellidos = "López Fernández",
            correo = "maria@duoc.cl",
            perfil = "Cliente",
            fechaNacimiento = "1996-02-17",
            region = "RM",
            comuna = "Las Condes",
            direccion = "Camino El Alba 321",
            descuentoVitalicio = true
        ),
        seedUser(
            id = 6,
            run = "222222222",
            nombre = "Javier",
            apellidos = "Rojas Díaz",
            correo = "jrojas@gmail.com",
            perfil = "Vendedor",
            fechaNacimiento = "1990-09-30",
            region = "Biobío",
            comuna = "Talcahuano",
            direccion = "Calle Colón 987",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 7,
            run = "333333333",
            nombre = "Constanza",
            apellidos = "Soto Herrera",
            correo = "constanza@profesor.duoc.cl",
            perfil = "Administrador",
            fechaNacimiento = "1985-12-05",
            region = "Valparaíso",
            comuna = "Quilpué",
            direccion = "Av. Los Carrera 147",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 8,
            run = "444444444",
            nombre = "Diego",
            apellidos = "García Morales",
            correo = "diegogm@gmail.com",
            perfil = "Cliente",
            fechaNacimiento = "2001-04-09",
            region = "RM",
            comuna = "Maipú",
            direccion = "Av. Pajaritos 654",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 9,
            run = "555555555",
            nombre = "Valentina",
            apellidos = "Ramírez Castro",
            correo = "valen@duoc.cl",
            perfil = "Vendedor",
            fechaNacimiento = "1993-08-14",
            region = "Valparaíso",
            comuna = "Villa Alemana",
            direccion = "Calle Central 852",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 10,
            run = "666666666",
            nombre = "Francisco",
            apellidos = "Fuentes Bravo",
            correo = "fran@profesor.duoc.cl",
            perfil = "Administrador",
            fechaNacimiento = "1984-03-28",
            region = "Biobío",
            comuna = "Chiguayante",
            direccion = "Pasaje Los Aromos 112",
            descuentoVitalicio = false
        ),
        seedUser(
            id = 11,
            run = "777777777",
            nombre = "Camila",
            apellidos = "Martínez Vargas",
            correo = "camila@gmail.com",
            perfil = "Cliente",
            fechaNacimiento = "2000-06-22",
            region = "RM",
            comuna = "Providencia",
            direccion = "Av. Providencia 3456",
            descuentoVitalicio = false
        )
    )

    val superAdmin: UserEntity = seededUsers.firstOrNull { it.isSystem }
        ?: seededUsers.first { it.isSuperAdmin }

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
