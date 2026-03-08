package com.dlucci.textbcc.ui

import android.content.Context
import android.provider.ContactsContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dlucci.textbcc.Screen
import com.dlucci.textbcc.model.ContactNumber
import com.dlucci.textbcc.viewmodel.TextBccViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun ContactScreen(navController: NavController, viewModel: TextBccViewModel = koinViewModel()) {
    val context = LocalContext.current
    var contacts by remember { mutableStateOf<List<ContactNumber>>(emptyList()) }
    LaunchedEffect(Unit) {
        contacts = withContext(Dispatchers.IO) {
            getContacts(context)
        }
    }
    val selectedContacts = remember { mutableStateListOf<ContactNumber>() }
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.selectableGroup().align(Alignment.Center)) {
            items(contacts) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.selectable(
                        selected = selectedContacts.contains(it),
                        onClick = {
                            if (selectedContacts.contains(it))
                                selectedContacts.remove(it)
                            else
                                selectedContacts.add(it)
                        },
                        role = Role.Checkbox
                    )
                ) {
                    RadioButton(
                        selected = selectedContacts.contains(it),
                        onClick = null
                    )
                    Text(text = it.name)
                    Text(text = it.phoneNumber)
                }
            }
        }
        Button(enabled = selectedContacts.isNotEmpty(), onClick = {
            viewModel.inputUsers(selectedContacts)
            navController.navigate(Screen.ComposeScreen.route)
        }, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)) {
            Text(text = "Next")
        }
    }
}

/**
 * TODO:  move to viewmodel
 */
private fun getContacts(context: Context): MutableList<ContactNumber> {
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
