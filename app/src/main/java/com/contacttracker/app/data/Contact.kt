package com.contacttracker.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Statuts de suivi possibles pour un contact.
 */
object ContactStatus {
    const val NEW = "New"
    const val CONTACTED = "Contacted"
    const val IN_PROGRESS = "In progress"
    const val DONE = "Done"

    val ALL = listOf(NEW, CONTACTED, IN_PROGRESS, DONE)
}

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val lastName: String = "",
    val firstName: String = "",
    val gender: String = "", // "M" or "F"

    val phone: String = "",
    val phoneSecondary: String = "",
    val email: String = "",

    val hostName: String = "",
    val hostPhone: String = "",
    val hostPhoneSecondary: String = "",

    val wantsPrayer: Boolean = false,
    val wantsOtherSituation: Boolean = false,

    val contactAtHome: Boolean = false,
    val contactAtOffice: Boolean = false,
    val contactAtChurch: Boolean = false,

    val status: String = ContactStatus.NEW,
    val notes: String = "",

    val createdAt: Long = System.currentTimeMillis()
) {
    val fullName: String
        get() = "$firstName $lastName".trim()
}
