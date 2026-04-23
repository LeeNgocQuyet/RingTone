package com.example.ringtone.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ringtone.R
import com.example.ringtone.domain.model.Ringtone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRingtoneClick: (String) -> Unit,
    onSetRingtone: (Ringtone) -> Unit,
    onToggleFavorite: (Ringtone) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScaffold(
        uiState = uiState,
        onSearchClick = onSearchClick,
        onSettingsClick = onSettingsClick,
        onRingtoneClick = onRingtoneClick,
        onSetRingtone = onSetRingtone,
        onToggleFavorite = onToggleFavorite
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    uiState: HomeUiState,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRingtoneClick: (String) -> Unit,
    onSetRingtone: (Ringtone) -> Unit,
    onToggleFavorite: (Ringtone) -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar(onSearchClick, onSettingsClick)
        }
    ) { innerPadding ->
        HomeContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            uiState = uiState,
            onRingtoneClick = onRingtoneClick,
            onSetRingtone = onSetRingtone,
            onToggleFavorite = onToggleFavorite
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onSearchClick: () -> Unit, onSettingsClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "RingTone",
                color = Color(0xFF4CAF50), // Green text
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onRingtoneClick: (String) -> Unit,
    onSetRingtone: (Ringtone) -> Unit,
    onToggleFavorite: (Ringtone) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Featured Section
        item {
            FeaturedCard()
        }

        // Category Section
        item {
            CategorySelector()
        }

        // Ringtone List
        items(uiState.ringtones) { ringtone ->
            RingtoneListItem(
                ringtone = ringtone,
                onClick = { onRingtoneClick(ringtone.id) },
                onSetClick = { onSetRingtone(ringtone) },
                onFavoriteToggle = { onToggleFavorite(ringtone) }
            )
        }
    }
}

@Composable
fun FeaturedCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298))
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Top Ringtone",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Best of the week",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Play", color = Color.Black)
                    }
                }

                // Vinyl Disc Style Image (Placeholder)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray)
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySelector() {
    val categories = listOf("All", "Nature", "Electronic", "Classical", "Pop", "Rock")
    var selectedCategory by remember { mutableStateOf("All") }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { selectedCategory = category },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFC8E6C9), // Light Green
                    selectedLabelColor = Color(0xFF2E7D32)
                ),
                border = null,
                shape = RoundedCornerShape(50)
            )
        }
    }
}

@Composable
fun RingtoneListItem(
    ringtone: Ringtone,
    onClick: () -> Unit,
    onSetClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.DarkGray)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Title + Duration
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = ringtone.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ringtone.duration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Actions
        Button(
            onClick = onSetClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)), // Purple
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text("Set", fontSize = 12.sp)
        }

        IconButton(onClick = {
            isFavorite = !isFavorite
            onFavoriteToggle()
        }) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else LocalContentColor.current
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomePreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface {
            HomeScaffold(
                uiState = HomeUiState(
                    ringtones = listOf(
                        Ringtone("1", "Summer Breeze", "Sunny Artist", "", "", "0:30", "Nature"),
                        Ringtone("2", "Digital Horizon", "Tech Beats", "", "", "0:25", "Electronic"),
                        Ringtone("3", "Morning Dew", "Calm Master", "", "", "0:45", "Classical")
                    )
                ),
                onSearchClick = {},
                onSettingsClick = {},
                onRingtoneClick = {},
                onSetRingtone = {},
                onToggleFavorite = {}
            )
        }
    }
}
