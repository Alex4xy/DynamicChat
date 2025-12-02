package com.alex.dynamicchat.features.chat.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.hilt.navigation.compose.hiltViewModel
import com.alex.dynamicchat.R
import com.alex.dynamicchat.features.chat.presentation.viewmodel.ChatViewModel
import com.alex.dynamicchat.ui.theme.DarkBackground
import com.alex.dynamicchat.ui.theme.DeepBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val viewModel: ChatViewModel = hiltViewModel()
    val resourceProvider = viewModel.resourceProvider

    val networkStatus = viewModel.networkStatus.collectAsState().value
    val chatState = viewModel.state.collectAsState()
    val inputText = viewModel.messageInput.collectAsState().value

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = viewModel.errorEvent) {
        viewModel.errorEvent.collect { errorMessage ->
            snackBarHostState.showSnackbar(errorMessage)
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = resourceProvider.getString(R.string.title_chat),
                        color = White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepBlue,
                    titleContentColor = White,
                    actionIconContentColor = White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hello",
                color = White
            )
        }
    }
}
