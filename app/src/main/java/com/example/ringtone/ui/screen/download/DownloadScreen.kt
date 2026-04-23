package com.example.ringtone.ui.screen.download

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ringtone.domain.model.Ringtone

// Colors from requirements
val LimeGreen = Color(0xFFD4FF5B)
val SoftPurple = Color(0xFFC5A3FF)
val DarkBackground = Color(0xFF121212)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadScreen(
    viewModel: DownloadViewModel,
    onSettingsClick: () -> Unit,
    onMoreClick: () -> Unit,
    onRingtoneClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    DownloadScaffold(
        uiState = uiState,
        onLinkChange = { viewModel.onLinkChanged(it) },
        onDownloadClick = { viewModel.downloadAudio() },
        onSettingsClick = onSettingsClick,
        onMoreClick = onMoreClick,
        onRingtoneClick = onRingtoneClick,
        onFavoriteToggle = { viewModel.toggleFavorite(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadScaffold(
    uiState: DownloadUiState,
    onLinkChange: (String) -> Unit,
    onDownloadClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMoreClick: () -> Unit,
    onRingtoneClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit
) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground),
                title = {
                    Text(
                        "Download",
                        color = LimeGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        DownloadContent(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onLinkChange = onLinkChange,
            onDownloadClick = onDownloadClick,
            onMoreClick = onMoreClick,
            onRingtoneClick = onRingtoneClick,
            onFavoriteToggle = onFavoriteToggle
        )
    }
}

@Composable
fun DownloadContent(
    modifier: Modifier = Modifier,
    uiState: DownloadUiState,
    onLinkChange: (String) -> Unit,
    onDownloadClick: () -> Unit,
    onMoreClick: () -> Unit,
    onRingtoneClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Hero Card: Download Tik Audio
        item {
            HeroDownloadCard(
                url = uiState.linkUrl,
                onUrlChange = onLinkChange,
                onDownloadClick = onDownloadClick
            )
        }

        // Download History Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Download History",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onMoreClick) {
                    Text("More >", color = LimeGreen)
                }
            }
        }

        // History List Items
        items(uiState.downloadHistory) { ringtone ->
            DownloadHistoryItem(
                ringtone = ringtone,
                isFavorite = uiState.favoriteIds.contains(ringtone.id),
                onClick = { onRingtoneClick(ringtone.id) },
                onFavoriteToggle = { onFavoriteToggle(ringtone.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroDownloadCard(
    url: String,
    onUrlChange: (String) -> Unit,
    onDownloadClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Download Tik Audio",
                color = SoftPurple,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Save your favorite TikTok sounds instantly",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = url,
                onValueChange = onUrlChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Paste Tik link here", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null, tint = LimeGreen) },
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SoftPurple,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Button(
                onClick = onDownloadClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SoftPurple),
                shape = RoundedCornerShape(50)
            ) {
                Text("Download Audio", color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Download, contentDescription = null, tint = Color.Black)
            }
        }
    }
}

@Composable
fun DownloadHistoryItem(
    ringtone: Ringtone,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail with Play Icon
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF252525)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = null,
                tint = LimeGreen,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title and Duration
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = ringtone.title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ringtone.duration,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Actions: Set and Favorite
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { /* Set Ringtone */ },
                colors = ButtonDefaults.buttonColors(containerColor = SoftPurple),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Set", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DownloadPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        DownloadScaffold(
            uiState = DownloadUiState(
                downloadHistory = listOf(
                    Ringtone("1", "Tik Viral Hit 2024", "Trending Music", "", "", "00:30", "TikTok"),
                    Ringtone("5", "Chill Beat Lofi", "Lofi Girl", "", "", "01:20", "Lofi"),
                    Ringtone("6", "Dramatic Impact", "SFX Master", "", "", "00:15", "SFX")
                )
            ),
            onLinkChange = {},
            onDownloadClick = {},
            onSettingsClick = {},
            onMoreClick = {},
            onRingtoneClick = {},
            onFavoriteToggle = {}
        )
    }
}
