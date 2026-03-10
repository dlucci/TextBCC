package com.dlucci.textbcc.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dlucci.textbcc.Screen
import com.dlucci.textbcc.model.ContactNumber
import com.dlucci.textbcc.persistence.ContactGroupEntity
import com.dlucci.textbcc.viewmodel.TextBccViewModel
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun ImportScreen(navController: NavController, viewModel: TextBccViewModel = koinViewModel()) {

    val contactPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
            if (!it.containsValue(false)) {
                navController.navigate(Screen.ContactScreen.route)
            }
        }
    val contacts by viewModel.contacts.collectAsState()
    LaunchedEffect(Unit) { viewModel.getAllContacts() }
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (contacts.isEmpty()) {
                ImportContactButton(onClick = {
                    contactPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.SEND_SMS
                        )
                    )
                })
            } else {
                ContactGroupList(
                    contacts, onClick = {
                        navController.navigate(Screen.ContactScreen.route)
                    },
                    onGroupClick = { group ->
                        viewModel.updateSelectedUsers(group)
                        navController.navigate(Screen.ComposeScreen.route)
                    },
                    onLongClicky = {contact ->
                        viewModel.delete(contact)
                    })
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContactGroupList(
    contacts: List<ContactGroupEntity>, onClick: () -> Unit,
    onGroupClick: (List<ContactNumber>) -> Unit,
    onLongClicky: (ContactGroupEntity)-> Unit
) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { TextBccFloatingActionButton(onClick = { onClick() }) },
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {

        LazyColumn {
            items(contacts) { contact ->
                Card(modifier = Modifier.combinedClickable(onLongClick = { onLongClicky(contact) }, onClick = {onGroupClick(contact.contacts)})) {
                    Text(
                        text = "${contact.groupName}:  ", fontWeight = FontWeight.Bold,
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        contact.contacts.forEach { person ->
                            Text(text = "${person.name}, ")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextBccFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() }
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
}

@Composable
fun ImportContactButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = "Import Contacts")
    }
}