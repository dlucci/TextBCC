package com.dlucci.textbcc.viewmodel

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.telephony.SmsManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlucci.textbcc.TextBccDeliveredBroadcastReceiver
import com.dlucci.textbcc.TextBccSentBroadcastReceiver
import com.dlucci.textbcc.model.ContactNumber
import com.dlucci.textbcc.persistence.ContactGroupEntity
import com.dlucci.textbcc.persistence.ContactNumberDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject


class TextBccViewModel : ViewModel() {
    private val database : ContactNumberDatabase by inject(ContactNumberDatabase::class.java)

    private val _selectedUsers = MutableStateFlow<List<ContactNumber>>(emptyList())
    val selectedUsers = _selectedUsers.asStateFlow()

    private val _contacts = MutableStateFlow<List<ContactGroupEntity>>(emptyList())
    val  contacts = _contacts.asStateFlow()

    fun updateSelectedUsers(users : List<ContactNumber>) {
        _selectedUsers.value = users
    }

    fun getContacts(context: Context): MutableList<ContactNumber> {
        val contacts = mutableListOf<ContactNumber>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC"
        )
        cursor.use {
            val nameIndex =
                it?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) as Int
            val phoneIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                contacts.add(
                    ContactNumber(
                        name = it.getString(nameIndex),
                        phoneNumber = it.getString(phoneIndex)
                    )
                )
            }
        }

        return contacts

    }


    fun sendMessage(message : String, context: Context) {
        val sendingIntent = Intent(context, TextBccSentBroadcastReceiver::class.java)
        val deliveredIntent = Intent(context, TextBccDeliveredBroadcastReceiver::class.java)
        val smsManager = context.getSystemService(SmsManager::class.java)
        selectedUsers.value.forEach {
            smsManager.sendTextMessage(
                it.phoneNumber, /*Delivery Address*/
                null, /*scAddress*/
                message, /*text*/
                PendingIntent.getBroadcast( /*sent Intent*/
                    context,
                    42,
                    sendingIntent,
                    PendingIntent.FLAG_MUTABLE
                ),
                PendingIntent.getBroadcast( /*delivery Intent*/
                    context,
                    42,
                    deliveredIntent,
                    PendingIntent.FLAG_MUTABLE
                ),
            )
        }
    }

    fun saveGroup(name : String) {
        val dao = database.contactDao()
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(ContactGroupEntity(groupName = name, contacts = selectedUsers.value))
        }

    }

    fun getAllContacts() {
        val dao = database.contactDao()
        viewModelScope.launch(Dispatchers.IO) {
             _contacts.value = dao.getAllGroups()
        }


    }

}