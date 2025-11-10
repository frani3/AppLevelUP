package com.applevelup.levepupgamerapp.presentation.data

/**
 * Static region and commune data based on Chilean administrative divisions.
 */
data class ChileanRegion(
    val name: String,
    val communes: List<String>
)

object ChileanRegionsData {
    val regions: List<ChileanRegion> = listOf(
        ChileanRegion(
            name = "Arica y Parinacota",
            communes = listOf("Arica", "Putre")
        ),
        ChileanRegion(
            name = "Tarapacá",
            communes = listOf("Iquique", "Alto Hospicio", "Pozo Almonte")
        ),
        ChileanRegion(
            name = "Antofagasta",
            communes = listOf("Antofagasta", "Calama", "Mejillones", "Tocopilla")
        ),
        ChileanRegion(
            name = "Atacama",
            communes = listOf("Copiapó", "Vallenar", "Caldera")
        ),
        ChileanRegion(
            name = "Coquimbo",
            communes = listOf("La Serena", "Coquimbo", "Ovalle", "Illapel")
        ),
        ChileanRegion(
            name = "Valparaíso",
            communes = listOf("Valparaíso", "Viña del Mar", "Quilpué", "Villa Alemana", "Quillota", "San Antonio")
        ),
        ChileanRegion(
            name = "Metropolitana de Santiago",
            communes = listOf(
                "Santiago",
                "Puente Alto",
                "Maipú",
                "Providencia",
                "Las Condes",
                "La Florida",
                "Ñuñoa",
                "Lo Barnechea",
                "Recoleta"
            )
        ),
        ChileanRegion(
            name = "O'Higgins",
            communes = listOf("Rancagua", "San Fernando", "Santa Cruz")
        ),
        ChileanRegion(
            name = "Maule",
            communes = listOf("Talca", "Curicó", "Linares", "Constitución")
        ),
        ChileanRegion(
            name = "Ñuble",
            communes = listOf("Chillán", "San Carlos", "Bulnes")
        ),
        ChileanRegion(
            name = "Biobío",
            communes = listOf(
                "Concepción",
                "Talcahuano",
                "San Pedro de la Paz",
                "Coronel",
                "Chiguayante",
                "Hualpén",
                "Los Ángeles"
            )
        ),
        ChileanRegion(
            name = "La Araucanía",
            communes = listOf("Temuco", "Padre Las Casas", "Villarrica", "Angol")
        ),
        ChileanRegion(
            name = "Los Ríos",
            communes = listOf("Valdivia", "La Unión", "Río Bueno")
        ),
        ChileanRegion(
            name = "Los Lagos",
            communes = listOf("Puerto Montt", "Osorno", "Castro", "Ancud", "Puerto Varas")
        ),
        ChileanRegion(
            name = "Aysén",
            communes = listOf("Coyhaique", "Puerto Aysén", "Chile Chico")
        ),
        ChileanRegion(
            name = "Magallanes",
            communes = listOf("Punta Arenas", "Puerto Natales", "Porvenir")
        )
    )

    val regionToCommunes: Map<String, List<String>> = regions.associate { it.name to it.communes }
}
