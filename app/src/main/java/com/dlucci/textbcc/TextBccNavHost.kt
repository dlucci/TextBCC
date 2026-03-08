package com.dlucci.textbcc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

import androidx.navigation.compose.rememberNavController
import com.dlucci.textbcc.ui.ComposeScreen
import com.dlucci.textbcc.ui.ContactScreen
import com.dlucci.textbcc.ui.ImportScreen
import com.dlucci.textbcc.viewmodel.TextBccViewModel
import kotlinx.serialization.Serializable

import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed class Screen(val route : String) {
    data object ImportScreen: Screen("import")
    data object ContactScreen: Screen("contacts")
    @Serializable data object ComposeScreen: Screen("compose")
}

private val route_name = "text_bcc"

@Composable
fun TextBccNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = route_name) {
        navigation(startDestination = Screen.ImportScreen.route, route = route_name){
            composable(route = Screen.ImportScreen.route) {
                ImportScreen(navController)
            }
            composable(route = Screen.ContactScreen.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(route_name)
                }
                val viewModel = koinViewModel<TextBccViewModel>(viewModelStoreOwner = parentEntry)
                ContactScreen(navController, viewModel)
            }
            composable(route = Screen.ComposeScreen.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(route_name)
                }
                val viewModel = koinViewModel<TextBccViewModel>(viewModelStoreOwner = parentEntry)
                ComposeScreen(navController, viewModel)
            }
        }
    }
}