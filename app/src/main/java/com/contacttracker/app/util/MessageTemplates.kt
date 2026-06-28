package com.contacttracker.app.util

import com.contacttracker.app.data.Contact

/**
 * Construit un message de relance pré-rempli en fonction des cases cochées.
 * Le message s'ouvre dans WhatsApp / SMS, modifiable avant l'envoi.
 */
object MessageTemplates {

    fun buildGreetingMessage(contact: Contact): String {
        val name = contact.firstName.ifBlank { contact.fullName }
        return when {
            contact.wantsPrayer ->
                "Bonjour $name, nous avons bien reçu votre demande de prière. " +
                    "Nous voulions prendre de vos nouvelles et prier avec vous. " +
                    "N'hésitez pas à nous partager comment vous allez."

            contact.wantsOtherSituation ->
                "Bonjour $name, nous aimerions reprendre contact avec vous suite à votre visite. " +
                    "Comment allez-vous depuis ?"

            else ->
                "Bonjour $name, nous sommes heureux de vous avoir rencontré(e) et " +
                    "nous voulions simplement prendre de vos nouvelles."
        }
    }
}
