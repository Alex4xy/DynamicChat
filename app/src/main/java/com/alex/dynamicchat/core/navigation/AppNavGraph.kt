package com.alex.dynamicchat.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alex.dynamicchat.features.chat.presentation.ui.screen.ChatScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "chat") {
        composable("chat") {
            ChatScreen()
        }
    }
}