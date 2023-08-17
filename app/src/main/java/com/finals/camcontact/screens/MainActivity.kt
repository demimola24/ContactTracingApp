package com.finals.camcontact.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finals.camcontact.*
import com.finals.camcontact.data.entity.ContactInfo
import com.finals.camcontact.screens.update.AddContactActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), ContactClickInterface, ContactClickDeleteInterface {
    private val contactRVAdapter = ContactRVAdapter(this, this, this)
    lateinit var viewModal: ContactViewModal
    lateinit var contactsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton

    override fun onResume() {
        super.onResume()

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ContactViewModal::class.java)
        viewModal.allContacts.observe(this, Observer { list ->
            list?.let {
                //on below line we are updating our list.
                contactRVAdapter.updateList(it)
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contactsRV = findViewById(R.id.contactsRV)
        addFAB = findViewById(R.id.idFAB)
        contactsRV.layoutManager = LinearLayoutManager(this)
        contactsRV.adapter = contactRVAdapter

        addFAB.setOnClickListener {
            //adding a click listner for fab button and opening a new intent to add a new contact.
            val intent = Intent(this@MainActivity, AddContactActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onContactClick(contact: ContactInfo) {
        //opening a new intent and passing a data to it.
        val intent = Intent(this@MainActivity, AddContactActivity::class.java)
        intent.putExtra("contactType", "Edit")
        intent.putExtra("contactName", contact.name)
        intent.putExtra("contactPhone", contact.phone)
        intent.putExtra("contactId", contact.id)
        startActivity(intent)
    }

    override fun onDeleteIconClick(contact: ContactInfo) {
        //in on contact click method we are calling delete method from our viw modal to delete our not.
        viewModal.deleteContact(contact)
        //displaying a toast message
        Toast.makeText(this, "${contact.name} Deleted", Toast.LENGTH_LONG).show()
    }

}