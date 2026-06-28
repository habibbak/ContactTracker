package com.contacttracker.app.util

/**
 * Les numéros sont saisis au format local gabonais (ex: 077123456).
 * WhatsApp a besoin du format international sans "+", sans espace (ex: 24177123456).
 */
object PhoneUtils {

    private const val DEFAULT_COUNTRY_CODE = "241" // Gabon

    fun toInternational(localNumber: String, countryCode: String = DEFAULT_COUNTRY_CODE): String {
        val digitsOnly = localNumber.filter { it.isDigit() }
        return when {
            digitsOnly.isEmpty() -> ""
            digitsOnly.startsWith(countryCode) -> digitsOnly
            digitsOnly.startsWith("0") -> countryCode + digitsOnly.substring(1)
            else -> countryCode + digitsOnly
        }
    }

    fun isValid(localNumber: String): Boolean = localNumber.any { it.isDigit() }
}
