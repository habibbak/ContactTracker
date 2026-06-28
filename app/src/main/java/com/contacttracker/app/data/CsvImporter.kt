package com.contacttracker.app.data

/**
 * Lit le contenu brut d'un fichier CSV et le transforme en liste de [Contact].
 *
 * Colonnes attendues, dans cet ordre exact :
 * Nom(s), Prenom(s), Sexe, Numéro de téléphone, Numéro de téléphone secondaire,
 * Adresse Email, Nom de celui qui vous a invité (Hôte), Numéro de téléphone de l'hôte,
 * Numéro de téléphone secondaire de l'hôte,
 * Besoin de prière pour mes besoins personnels (Oui/Non), Autre situation particulière (Oui/Non),
 * Contact à la maison (Oui/Non), Contact au bureau (Oui/Non), Contact dans l'une de nos églises (Oui/Non)
 */
object CsvImporter {

    private const val EXPECTED_COLUMNS = 14

    fun parse(csvText: String): List<Contact> {
        val cleanText = csvText.removePrefix("\uFEFF") // retire le BOM s'il est présent
        val lines = cleanText.lines().filter { it.isNotBlank() }
        if (lines.isEmpty()) return emptyList()

        // La première ligne est l'en-tête, on la saute.
        val dataLines = lines.drop(1)

        return dataLines.mapNotNull { line ->
            val cols = parseLine(line)
            if (cols.size < EXPECTED_COLUMNS) return@mapNotNull null

            Contact(
                lastName = cols[0].trim(),
                firstName = cols[1].trim(),
                gender = cols[2].trim(),
                phone = cols[3].trim(),
                phoneSecondary = cols[4].trim(),
                email = cols[5].trim(),
                hostName = cols[6].trim(),
                hostPhone = cols[7].trim(),
                hostPhoneSecondary = cols[8].trim(),
                wantsPrayer = isYes(cols[9]),
                wantsOtherSituation = isYes(cols[10]),
                contactAtHome = isYes(cols[11]),
                contactAtOffice = isYes(cols[12]),
                contactAtChurch = isYes(cols[13]),
                status = ContactStatus.NEW
            )
        }
    }

    private fun isYes(value: String): Boolean =
        value.trim().equals("Oui", ignoreCase = true)

    /**
     * Découpe une ligne CSV en colonnes, en respectant les champs entourés de guillemets
     * (utile si un champ contient une virgule).
     */
    private fun parseLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var insideQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            when {
                c == '"' -> insideQuotes = !insideQuotes
                c == ',' && !insideQuotes -> {
                    result.add(current.toString())
                    current.clear()
                }
                else -> current.append(c)
            }
            i++
        }
        result.add(current.toString())
        return result
    }
}
