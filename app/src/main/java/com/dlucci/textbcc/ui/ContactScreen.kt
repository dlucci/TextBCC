package com.dlucci.textbcc.ui

import android.Manifest
import android.annotation.SuppressLint
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dlucci.textbcc.model.ContactNumber

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun ContactScreen() {
    val context = LocalContext.current
    val contacts = remember { mutableStateListOf<ContactNumber>()}

    val contactPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
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
                            ContactNumber(name = it.getString(nameIndex), phoneNumber = it.getString(phoneIndex))
                        )
                    }
                }
            }
        }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(contacts.size == 0) {
                ImportContactButton(onClick = {
                    contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                })
            } else {
                ContactList(contacts)
            }
        }
    }
}

@Composable
fun ContactList(contacts: List<ContactNumber>) {
    val selectedContacts = remember {  mutableStateListOf<ContactNumber>() }
    LazyColumn(modifier = Modifier.selectableGroup()) {
        items(contacts) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.selectable(
                    selected = selectedContacts.contains(it),
                    onClick = {
                        if(selectedContacts.contains(it))
                            selectedContacts.remove(it)
                        else
                            selectedContacts.add(it)
                    },
                    role = Role.Checkbox
                )) {
                RadioButton(
                    selected = selectedContacts.contains(it),
                    onClick = null
                )
                Text(text = it.name)
                Text(text = it.phoneNumber)
            }
        }
    }
    Button(enabled = selectedContacts.size > 0,
        onClick = { SendMessageScreen(selectedContacts) }) {
        Text(text = "Next")
    }
}

@Composable
fun SendMessageScreen(selectedContacts: List<ContactNumber>) {
    TODO("Not yet implemented")
}


@Composable
fun ImportContactButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = "Import Contacts")
    }
}

@Preview
@Composable
fun PreviewContactScreen() {
    ContactScreen()
}