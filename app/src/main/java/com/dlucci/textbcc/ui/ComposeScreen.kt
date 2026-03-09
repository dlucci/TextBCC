package com.dlucci.textbcc.ui

import android.app.PendingIntent
import android.content.Intent
import android.telephony.SmsManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.dlucci.textbcc.TextBccDeliveredBroadcastReceiver
import com.dlucci.textbcc.TextBccSentBroadcastReceiver
import com.dlucci.textbcc.viewmodel.TextBccViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ComposeScreen(navController: NavController, viewModel: TextBccViewModel = koinViewModel()) {
    val context = LocalContext.current

    var text by remember { mutableStateOf("") }
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
            SaveButton(text, onClick = {
                val sendingIntent = Intent(context, TextBccSentBroadcastReceiver::class.java)
                val deliveredIntent = Intent(context, TextBccDeliveredBroadcastReceiver::class.java)
                val smsManager = context.getSystemService(SmsManager::class.java)
                viewModel.selectedUsers.value.forEach {
                    smsManager.sendTextMessage(
                        it.phoneNumber, /*Delivery Address*/
                        null, /*scAddress*/
                        text, /*text*/
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
            })
        }
    }
}

@Composable
fun SaveButton(text: String, onClick: () -> Unit) {
    Button(enabled = text.isNotEmpty(), onClick = { onClick() }) {
        Text("Send")
    }
}
