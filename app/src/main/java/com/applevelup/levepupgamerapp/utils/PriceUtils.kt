package com.applevelup.levepupgamerapp.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object PriceUtils {
    /**
     * Formatea un valor Double a pesos chilenos.
     * Ej: 99990.0 -> "$99.990"
     *     1249990.0 -> "$1.249.990"
     */
    fun formatPriceCLP(price: Double): String {
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = '.'
        val formatter = DecimalFormat("#,###", symbols)
        return "$" + formatter.format(price.toInt())
    }
}
