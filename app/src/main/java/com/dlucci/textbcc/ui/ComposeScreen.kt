package com.dlucci.textbcc.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.dlucci.textbcc.Screen
import com.dlucci.textbcc.viewmodel.TextBccViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ComposeScreen(navController: NavController, viewModel: TextBccViewModel = koinViewModel()) {
    val context = LocalContext.current

    var text by remember { mutableStateOf("") }
    var clicked by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column {
            LazyRow {
                items(viewModel.selectedUsers.value) {
                    SuggestionChip(
                        label = { Text(text = it.name) },
                        onClick = {})
                }
            }
            TextField(
                value = text,
                onValueChange = {
                    text = it
                }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                SendButton(text, onClick = {
                    viewModel.sendMessage(message = text, context = context)
                    navController.navigate(Screen.ImportScreen.route) {
                        popUpTo(Screen.ImportScreen.route) {
                            inclusive = false
                        }
                    }
                })
                SaveButton { clicked = true }
            }
        }
    }

    if (clicked) {
        SaveGroupNameDialog(onConfirm = { groupName -> viewModel.saveGroup(groupName) }, onDismiss = { clicked = false })
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveGroupNameDialog(onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var text by remember { mutableStateOf("") }
    BasicAlertDialog(onDismissRequest = {onDismiss()},
        properties = DialogProperties(windowTitle = "Save Group?")
    ) {
        Column {
            TextField(value = text, modifier = Modifier.padding(horizontal = 15.dp).height(300.dp), onValueChange = {
                text = it
            })
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                TextButton(onClick = { onConfirm(text) }, enabled = text.isNotEmpty()) {
                    Text(text = "Save")
                }
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Dismiss")
                }
            }
        }
    }
}

@Composable
fun SendButton(text: String, onClick: () -> Unit) {
    Button(enabled = text.isNotEmpty(), onClick = { onClick() }) {
        Text("Send")
    }
}

@Composable
fun SaveButton(onClick: () -> Unit) {
    Button(enabled = true, onClick = { onClick() }) {
        Text("Save Group")
    }
}
