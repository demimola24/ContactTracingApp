package com.finals.camcontact.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
class ContactInfo (@ColumnInfo(name = "name") val name :String, @ColumnInfo(name = "phone") val phone :String,
                   @ColumnInfo(name = "timestamp")val timeStamp :String) {
    @PrimaryKey(autoGenerate = true) var id = 0

}