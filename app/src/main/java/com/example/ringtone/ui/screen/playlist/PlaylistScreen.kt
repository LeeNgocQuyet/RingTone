package com.example.ringtone.ui.screen.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ringtone.R

private val LimeGreen = Color(0xFFD4FF5B)
private val SoftPurple = Color(0xFFC5A3FF)

@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    onSettingsClick: () -> Unit,
    onDownloadAudioClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    PlaylistScaffold(
        uiState = uiState,
        onTabSelected = { viewModel.onTabSelected(it) },
        onSettingsClick = onSettingsClick,
        onDownloadAudioClick = onDownloadAudioClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScaffold(
    uiState: PlaylistUiState,
    onTabSelected: (PlaylistTab) -> Unit,
    onSettingsClick: () -> Unit,
    onDownloadAudioClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                title = {
                    Text(
                        text = "Playlist",
                        color = LimeGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Tabs
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlaylistTabItem(
                    title = "Downloads",
                    selected = uiState.selectedTab == PlaylistTab.Downloads,
                    onClick = { onTabSelected(PlaylistTab.Downloads) }
                )
                PlaylistTabItem(
                    title = "Favorites",
                    selected = uiState.selectedTab == PlaylistTab.Favorites,
                    onClick = { onTabSelected(PlaylistTab.Favorites) }
                )
            }

            // Empty State
            if (uiState.items.isEmpty()) {
                EmptyPlaylistView(onDownloadAudioClick)
            } else {
                // List of items
            }
        }
    }
}

@Composable
fun PlaylistTabItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) LimeGreen else Color(0xFF1E1E1E),
            contentColor = if (selected) Color.Black else Color.White
        ),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
        modifier = Modifier.height(36.dp)
    ) {
        Text(text = title, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun EmptyPlaylistView(onDownloadAudioClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Placeholder for the "sad face" icons in the image
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel), // Placeholder
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Empty here",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Download audio from Tik to build your collection.",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onDownloadAudioClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SoftPurple),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Download Audio",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlaylistPreview() {
    PlaylistScaffold(
        uiState = PlaylistUiState(),
        onTabSelected = {},
        onSettingsClick = {},
        onDownloadAudioClick = {}
    )
}
