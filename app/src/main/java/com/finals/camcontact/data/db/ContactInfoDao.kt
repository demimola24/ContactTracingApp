package com.finals.camcontact.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.finals.camcontact.data.entity.ContactInfo

@Dao
interface ContactInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note : ContactInfo)

    @Update
    suspend fun update(note: ContactInfo)

    @Delete
    suspend fun delete(note: ContactInfo)

    @Query("Select * from contact_table order by id ASC")
    fun getAllContacts(): LiveData<List<ContactInfo>>


}