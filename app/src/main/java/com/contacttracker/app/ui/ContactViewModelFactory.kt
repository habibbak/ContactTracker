package com.contacttracker.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.contacttracker.app.data.ContactRepository

class ContactViewModelFactory(private val repository: ContactRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            return ContactViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel inconnu : ${modelClass.name}")
    }
}
