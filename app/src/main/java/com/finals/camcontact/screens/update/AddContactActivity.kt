package com.finals.camcontact.screens.update

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.finals.camcontact.R
import com.finals.camcontact.data.entity.ContactInfo
import com.finals.camcontact.screens.ContactViewModal
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class AddContactActivity : AppCompatActivity() {
    lateinit var contactName: TextInputLayout
    lateinit var contactPhone: TextInputLayout
    lateinit var saveBtn: Button
    lateinit var importBtn: Button

    //contact permission code
    private val CONTACT_PERMISSION_CODE = 1;
    //contact pick code
    private val CONTACT_PICK_CODE = 2



    //on below line we are creating variable for viewmodal and and integer for our contact id.
    lateinit var viewModal: ContactViewModal
    var contactID = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_contact)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ContactViewModal::class.java)

        //on below line we are initializing all our variables.
        contactName = findViewById(R.id.tl_name)
        contactPhone = findViewById(R.id.tl_phone)
        saveBtn = findViewById(R.id.idBtn)
        importBtn  = findViewById(R.id.idBtnImport)

        //on below line we are getting data passsed via an intent.
        val contactType = intent.getStringExtra("contactType")
        if (contactType.equals("Edit")) {
            //on below line we are setting data to edit text.
            val name = intent.getStringExtra("contactName")
            val number = intent.getStringExtra("contactPhone")
            contactID = intent.getIntExtra("contactId", -1)
            saveBtn.setText("Update Contact")
            contactName.editText?.setText(name)
            contactPhone.editText?.setText(number)
        } else {
            saveBtn.setText("Save Contact")
        }

        importBtn.setOnClickListener {
            //check permission allowed or not
            if (checkContactPermission()){
                //allowed
                pickContact()
            }
            else{
                //not allowed, request
                requestContactPermission()
            }
        }


        saveBtn.setOnClickListener {
            //on below line we are getting name and phone from edit text.
            val name = contactName.editText?.text.toString()
            val phone = contactPhone.editText?.text.toString()
            //on below line we are checking the type and then saving or updating the data.
            if (contactType.equals("Edit")) {
                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    val sdf = SimpleDateFormat("HH:mm dd MMM, yyyy")
                    val currentDateAndTime: String = sdf.format(Date())
                    val updatedContact = ContactInfo(name, phone, currentDateAndTime)
                    updatedContact.id = contactID
                    viewModal.updateContact(updatedContact)
                    Toast.makeText(this, "Contact updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    val sdf = SimpleDateFormat(" HH:mm dd MMM, yyyy")
                    val currentDateAndTime: String = sdf.format(Date())
                    //if the string is not empty we are calling a add contact method to add data to our room database.
                    viewModal.addContact(ContactInfo(name, phone, currentDateAndTime))
                    Toast.makeText(this, "$name added..", Toast.LENGTH_LONG).show()
                }
            }
            this.finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        return if (id == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun checkContactPermission(): Boolean{
        //check if permission was granted/allowed or not, returns true if granted/allowed, false if not
        return  ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactPermission(){
        //request the READ_CONTACTS permission
        val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
        ActivityCompat.requestPermissions(this, permission, CONTACT_PERMISSION_CODE)
    }

    private fun pickContact(){
        //intent ti pick contact
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, CONTACT_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //handle permission request results || calls when user from Permission request dialog presses Allow or Deny
        if (requestCode == CONTACT_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //permission granted, can pick contact
                pickContact()
            }
            else{
                //permission denied, cann't pick contact, just show message
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //handle intent results || calls when user from Intent (Contact Pick) picks or cancels pick contact
        if (resultCode == RESULT_OK){
            //calls when user click a contact from contacts (intent) list
            if (requestCode == CONTACT_PICK_CODE){


                val cursor1: Cursor
                val cursor2: Cursor?

                //get data from intent
                val uri = data!!.data
                cursor1 = contentResolver.query(uri!!, null, null, null, null)!!
                if (cursor1.moveToFirst()){
                    //get contact details
                    val contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    val idResultHold = idResults.toInt()

                    contactName.editText?.setText(name)

                    if (idResultHold == 1){
                        cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+contactId,
                            null,
                            null
                        )
                        //a contact may have multiple phone numbers
                        while (cursor2!!.moveToNext()){
                            //get phone number
                            val contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                             contactPhone.editText?.setText(contactNumber)
                        }
                        cursor2.close()
                    }
                    cursor1.close()
                }
            }

        }
        else{
            //cancelled picking contact
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


}