package com.contacttracker.app.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.net.URLEncoder

object IntentUtils {

    fun openWhatsApp(context: Context, phone: String, message: String) {
        val international = PhoneUtils.toInternational(phone)
        if (international.isBlank()) {
            toast(context, "Numéro de téléphone manquant")
            return
        }
        val encodedMessage = URLEncoder.encode(message, "UTF-8")
        val url = "https://wa.me/$international?text=$encodedMessage"
        tryStart(context, Intent(Intent.ACTION_VIEW, Uri.parse(url)), "WhatsApp introuvable")
    }

    fun openSms(context: Context, phone: String, message: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone")).apply {
            putExtra("sms_body", message)
        }
        tryStart(context, intent, "Application SMS introuvable")
    }

    fun dialPhone(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        tryStart(context, intent, "Impossible d'ouvrir le téléphone")
    }

    fun sendEmail(context: Context, email: String, subject: String, body: String) {
        if (email.isBlank()) {
            toast(context, "Adresse email manquante")
            return
        }
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).apply {
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        tryStart(context, intent, "Application email introuvable")
    }

    private fun tryStart(context: Context, intent: Intent, errorMessage: String) {
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            toast(context, errorMessage)
        }
    }

    private fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
