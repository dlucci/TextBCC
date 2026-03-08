package com.dlucci.textbcc.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dlucci.textbcc.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun ImportScreen(navController: NavController) {

    val contactPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                navController.navigate(Screen.ContactScreen.route)
            }
        }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ImportContactButton(onClick = {
                contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            })
        }
    }
}

@Composable
fun ImportContactButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = "Import Contacts")
    }
}