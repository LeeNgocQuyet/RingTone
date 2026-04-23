package com.example.ringtone.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ringtone.domain.model.Ringtone

@Composable
fun HomeScreen(viewModel: HomeViewModel, onPlayClick: (Ringtone) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onPlayClick = onPlayClick
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onPlayClick: (Ringtone) -> Unit
) {
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(uiState.ringtones) { ringtone ->
                RingtoneItem(ringtone, onPlayClick)
            }
        }
    }
}

@Composable
fun RingtoneItem(ringtone: Ringtone, onPlayClick: (Ringtone) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = ringtone.title, style = MaterialTheme.typography.titleMedium)
                Text(text = ringtone.artist, style = MaterialTheme.typography.bodySmall)
            }
            Text(text = ringtone.duration, modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { onPlayClick(ringtone) }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeContent(
        uiState = HomeUiState(
            ringtones = listOf(
                Ringtone("1", "Preview Ringtone", "Artist", "", "", "0:30", "Test")
            )
        ),
        onPlayClick = {}
    )
}
