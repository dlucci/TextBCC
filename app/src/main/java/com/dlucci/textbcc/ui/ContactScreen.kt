package com.dlucci.textbcc.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
            viewModel.getContacts(context)
        }
    }
    var contactList by remember { mutableStateOf<List<ContactNumber>>(emptyList()) }
    val selectedContacts = remember { mutableStateListOf<ContactNumber>() }
    LaunchedEffect(contacts) {
        contactList = contacts
    }
    Scaffold(topBar = {SearchBar(contacts, onQueryChanged = {
        filteredContacts ->
           contactList = filteredContacts
    })}) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(modifier = Modifier.selectableGroup()) {
                items(contactList) {
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
                viewModel.updateSelectedUsers(selectedContacts)
                navController.navigate(Screen.ComposeScreen.route)
            }, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
fun SearchBar(contacts: List<ContactNumber>, onQueryChanged: (List<ContactNumber>) -> Unit) {
    var text by remember { mutableStateOf("") }
    Surface(modifier = Modifier.fillMaxWidth().statusBarsPadding(),
        tonalElevation = 3.dp) {
        TextField(
            value = text,
            onValueChange = { query ->
                text = query
                val filterList =
                    contacts.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
                onQueryChanged(filterList)
            },
        )
    }
}
