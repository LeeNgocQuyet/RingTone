package com.example.ringtone.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.ringtone.ui.screen.download.SoftPurple

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onRingtoneClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchScaffold(
        uiState = uiState,
        onQueryChange = { viewModel.onSearchQueryChanged(it) },
        onBackClick = onBackClick,
        onHistoryRemove = { viewModel.removeHistoryItem(it) },
        onRingtoneClick = onRingtoneClick,
        onFavoriteToggle = { viewModel.toggleFavorite(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScaffold(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onHistoryRemove: (String) -> Unit,
    onRingtoneClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 8.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                SearchBar(
                    query = uiState.query,
                    onQueryChange = onQueryChange
                )
            }
        }
    ) { innerPadding ->
        SearchContent(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onHistoryRemove = onHistoryRemove,
            onRingtoneClick = onRingtoneClick,
            onFavoriteToggle = onFavoriteToggle
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        placeholder = { Text("Search ringtones...", color = Color.Gray, fontSize = 14.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFD4FF5B)) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SoftPurple,
            unfocusedBorderColor = Color.DarkGray,
            focusedContainerColor = Color.Black,
            unfocusedContainerColor = Color.Black
        ),
        singleLine = true
    )
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onHistoryRemove: (String) -> Unit,
    onRingtoneClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // History Section
        if (uiState.query.isEmpty()) {
            item {
                Text(
                    text = "History",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.history.forEach { item ->
                        HistoryChip(text = item, onRemove = { onHistoryRemove(item) })
                    }
                }
            }
        }

        // Suggestions or Results Header
        item {
            Text(
                text = if (uiState.query.isEmpty()) "You might like these" else "Search Results",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        // List
        val displayList = if (uiState.query.isEmpty()) uiState.suggestions else uiState.results
        items(displayList) { ringtone ->
            SearchListItem(
                ringtone = ringtone,
                isFavorite = uiState.favoriteIds.contains(ringtone.id),
                onClick = { onRingtoneClick(ringtone.id) },
                onFavoriteToggle = { onFavoriteToggle(ringtone.id) }
            )
        }
    }
}

@Composable
fun HistoryChip(text: String, onRemove: () -> Unit) {
    Surface(
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() }
            )
        }
    }
}

@Composable
fun SearchListItem(
    ringtone: Ringtone,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail with Vinyl look
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            // Vinyl pattern (simple box for now)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFFD4FF5B), modifier = Modifier.align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = ringtone.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = ringtone.duration, color = Color.Gray, fontSize = 12.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { /* Set logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC5A3FF)),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Set", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = { content() }
    )
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchPreview() {
    SearchScaffold(
        uiState = SearchUiState(
            suggestions = listOf(Ringtone("1", "Tik Viral Hit 2024", "", "", "", "00:30", "")),
            history = listOf("Lo-fi", "Chill", "Techno")
        ),
        onQueryChange = {},
        onBackClick = {},
        onHistoryRemove = {},
        onRingtoneClick = {},
        onFavoriteToggle = {}
    )
}
