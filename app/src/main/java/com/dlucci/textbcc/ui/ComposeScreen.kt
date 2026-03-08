package com.dlucci.textbcc.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dlucci.textbcc.viewmodel.TextBccViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ComposeScreen(navController: NavController, viewModel: TextBccViewModel = koinViewModel()){
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(viewModel.selectedUsers.value) {
                Text(text = it.name)
            }
        }
    }
}