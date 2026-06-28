package com.contacttracker.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY createdAt DESC")
    fun getAllContacts(): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Long): Contact?

    @Insert
    suspend fun insertContact(contact: Contact): Long

    @Insert
    suspend fun insertAll(contacts: List<Contact>)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)
}
