package com.example.ringtone.ui.screen.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ringtone.domain.model.Ringtone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingtoneListScreen(
    type: String,
    viewModel: RingtoneListViewModel,
    onBackClick: () -> Unit,
    onPlayClick: (Ringtone) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(type) {
        viewModel.loadRingtones(type)
    }

    RingtoneListContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onPlayClick = onPlayClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingtoneListContent(
    uiState: RingtoneListUiState,
    onBackClick: () -> Unit,
    onPlayClick: (Ringtone) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.ringtones) { ringtone ->
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RingtoneListPreview() {
    RingtoneListContent(
        uiState = RingtoneListUiState(
            title = "Favorites",
            ringtones = listOf(
                Ringtone("1", "Favorite Ringtone", "Artist", "", "", "0:30", "Test")
            )
        ),
        onBackClick = {},
        onPlayClick = {}
    )
}
