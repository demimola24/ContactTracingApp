package com.finals.camcontact.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.finals.camcontact.data.entity.ContactInfo

@Database(entities = arrayOf(ContactInfo::class), version = 1, exportSchema = false)
abstract class ContactInfoDatabase : RoomDatabase() {

    abstract fun getContactsDao(): ContactInfoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ContactInfoDatabase? = null

        fun getDatabase(context: Context): ContactInfoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactInfoDatabase::class.java,
                    "contact_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


}