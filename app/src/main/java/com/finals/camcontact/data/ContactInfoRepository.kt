package com.finals.camcontact.data

import androidx.lifecycle.LiveData
import com.finals.camcontact.data.entity.ContactInfo
import com.finals.camcontact.data.db.ContactInfoDao

class ContactInfoRepository(private val contactDao: ContactInfoDao) {


    val allContact: LiveData<List<ContactInfo>> = contactDao.getAllContacts()

    suspend fun insert(contact: ContactInfo) {
        contactDao.insert(contact)
    }
    suspend fun delete(contact: ContactInfo){
        contactDao.delete(contact)
    }

    suspend fun update(contact: ContactInfo){
        contactDao.update(contact)
    }
}