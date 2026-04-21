package com.example.ringtone.ui.home

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


@Preview(showBackground = true, name = "Màn hình chính - Đang tải")
@Composable
fun PreviewHomeContentLoading() {
    HomeContent(
        uiState = HomeUiState(isLoading = true)
    )
}

@Preview(showBackground = true, name = "Màn hình chính - Trống")
@Composable
fun PreviewHomeContentEmpty() {
    HomeContent(
        uiState = HomeUiState(ringtones = emptyList(), isLoading = false)
    )
}

@Preview(showBackground = true, name = "Màn hình chính - Có dữ liệu")
@Composable
fun PreviewHomeContentPopulated() {
    val mockRingtones = listOf(
        Ringtone(id = "1", title = "Nhạc chuông 1", duration = "0:30"),
        Ringtone(id = "2", title = "Nhạc chuông 2", duration = "0:25"),
        Ringtone(id = "3", title = "Nhạc chuông 3", duration = "0:15")
    )

    // Bạn có thể chỉnh sửa trực tiếp dữ liệu giả ở đây để xem UI thay đổi thế nào
    HomeContent(
        uiState = HomeUiState(
            ringtones = mockRingtones,
            isLoading = false
        )
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onRingtoneClick: (Ringtone) -> Unit = {}
) {
    // Giao diện của bạn ở đây (LazyColumn, v.v.)
    // Ví dụ đơn giản:
    if (uiState.isLoading) {
        // Show Loading
    } else {
        // Show List
    }
}
