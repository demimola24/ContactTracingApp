package com.finals.camcontact.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.finals.camcontact.data.ContactInfoRepository
import com.finals.camcontact.data.entity.ContactInfo
import com.finals.camcontact.data.db.ContactInfoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModal(application: Application) : AndroidViewModel(application) {
    val allContacts: LiveData<List<ContactInfo>>
    val repository: ContactInfoRepository

    init {
        val dao = ContactInfoDatabase.getDatabase(application).getContactsDao()
        repository = ContactInfoRepository(dao)
        allContacts = repository.allContact
    }

    fun deleteContact(contact: ContactInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contact)
    }

    fun updateContact(contact: ContactInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(contact)
    }

    fun addContact(contact: ContactInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contact)
    }
}