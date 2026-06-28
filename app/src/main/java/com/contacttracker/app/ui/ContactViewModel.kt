package com.contacttracker.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contacttracker.app.data.Contact
import com.contacttracker.app.data.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val allContacts: StateFlow<List<Contact>> = repository.allContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredContacts: StateFlow<List<Contact>> = combine(allContacts, _searchQuery) { contacts, query ->
        if (query.isBlank()) {
            contacts
        } else {
            contacts.filter { it.fullName.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _importMessage = MutableStateFlow<String?>(null)
    val importMessage: StateFlow<String?> = _importMessage

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addContact(contact: Contact) = viewModelScope.launch {
        repository.insert(contact)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch {
        repository.update(contact)
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch {
        repository.delete(contact)
    }

    suspend fun getContactById(id: Long): Contact? = repository.getById(id)

    fun importContacts(contacts: List<Contact>) = viewModelScope.launch {
        if (contacts.isEmpty()) {
            _importMessage.value = "Aucun contact trouvé dans ce fichier"
        } else {
            repository.insertAll(contacts)
            _importMessage.value = "${contacts.size} contact(s) importé(s)"
        }
    }

    fun clearImportMessage() {
        _importMessage.value = null
    }
}
