package com.alex.dynamicchat.core.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.alex.dynamicchat.core.navigation.AppNavGraph
import com.alex.dynamicchat.ui.theme.DynamicChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DynamicChatTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}