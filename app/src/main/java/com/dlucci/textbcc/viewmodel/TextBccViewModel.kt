package com.dlucci.textbcc.viewmodel

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import com.dlucci.textbcc.model.ContactNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class TextBccViewModel : ViewModel() {

    private val _selectedUsers = MutableStateFlow<List<ContactNumber>>(emptyList())
    val selectedUsers = _selectedUsers.asStateFlow()

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


}