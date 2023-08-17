package com.finals.camcontact.screens

import android.R.attr.phoneNumber
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.finals.camcontact.R
import com.finals.camcontact.data.entity.ContactInfo


class ContactRVAdapter(
    val context: Context,
    val contactClickDeleteInterface: ContactClickDeleteInterface,
    val contactClickInterface: ContactClickInterface
) :
    RecyclerView.Adapter<ContactRVAdapter.ViewHolder>() {

    private val allContacts = ArrayList<ContactInfo>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.idTVContactName)
        val number = itemView.findViewById<TextView>(R.id.idTVContactNumber)
        val delete = itemView.findViewById<TextView>(R.id.idTVDelete)
        val call = itemView.findViewById<ImageView>(R.id.idIVCall)
        val edit = itemView.findViewById<TextView>(R.id.idTVEdit)
        val message = itemView.findViewById<TextView>(R.id.idTVMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.setText(allContacts[position].name)
        holder.number.setText(allContacts[position].phone)
        holder.delete.setOnClickListener {
            contactClickDeleteInterface.onDeleteIconClick(allContacts[position])
        }

        holder.message.setOnClickListener {
            val intentDial = Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + allContacts[position].phone))
            intentDial.putExtra("sms_body", "Message from the contact app")
            context.startActivity(intentDial)
        }
        holder.call.setOnClickListener {
            val intentDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + allContacts[position].name))
            context.startActivity(intentDial)
        }

        holder.edit.setOnClickListener {
            contactClickInterface.onContactClick(allContacts.get(position))
        }
    }

    override fun getItemCount(): Int {
        //on below line we are returning our list size.
        return allContacts.size
    }

    //below method is use to update our list of contacts.
    fun updateList(newList: List<ContactInfo>) {
        allContacts.clear()
        allContacts.addAll(newList)
        notifyDataSetChanged()
    }

}

interface ContactClickDeleteInterface {
    //creating a method for click action on delete image view.
    fun onDeleteIconClick(contact: ContactInfo)
}

interface ContactClickInterface {
    //creating a method for click action on recycler view item for updating it.
    fun onContactClick(contact: ContactInfo)
}