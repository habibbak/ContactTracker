package com.contacttracker.app.data

import kotlinx.coroutines.flow.Flow

class ContactRepository(private val dao: ContactDao) {

    val allContacts: Flow<List<Contact>> = dao.getAllContacts()

    suspend fun insert(contact: Contact): Long = dao.insertContact(contact)

    suspend fun insertAll(contacts: List<Contact>) = dao.insertAll(contacts)

    suspend fun update(contact: Contact) = dao.updateContact(contact)

    suspend fun delete(contact: Contact) = dao.deleteContact(contact)

    suspend fun getById(id: Long): Contact? = dao.getContactById(id)
}
