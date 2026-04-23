package com.example.ringtone.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.ui.component.RingtoneListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRingtoneClick: (String) -> Unit,
    onSetRingtone: (Ringtone) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScaffold(
        uiState = uiState,
        onSearchClick = onSearchClick,
        onSettingsClick = onSettingsClick,
        onRingtoneClick = onRingtoneClick,
        onSetRingtone = onSetRingtone,
        onToggleFavorite = onToggleFavorite,
        onCategorySelected = { viewModel.onCategorySelected(it) }
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
    onToggleFavorite: (String) -> Unit,
    onCategorySelected: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
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
            onToggleFavorite = onToggleFavorite,
            onCategorySelected = onCategorySelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onSearchClick: () -> Unit, onSettingsClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
        title = {
            Text(
                text = "RingTone",
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
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
    onToggleFavorite: (String) -> Unit,
    onCategorySelected: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item { FeaturedCard() }

        item {
            CategorySelector(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = onCategorySelected
            )
        }

        items(uiState.ringtones) { ringtone ->
            RingtoneListItem(
                ringtone = ringtone,
                isFavorite = uiState.favoriteIds.contains(ringtone.id),
                onClick = { onRingtoneClick(ringtone.id) },
                onSetClick = { onSetRingtone(ringtone) },
                onFavoriteToggle = { onToggleFavorite(ringtone.id) }
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
                    Text(text = "Top Ringtone", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(text = "Best of the week", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Play", color = Color.Black)
                    }
                }
                Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.Black), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(30.dp).clip(CircleShape).background(Color.DarkGray))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFC8E6C9),
                    selectedLabelColor = Color(0xFF2E7D32),
                    labelColor = Color.White,
                    containerColor = Color(0xFF1E1E1E)
                ),
                border = null,
                shape = RoundedCornerShape(50)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomePreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        HomeScaffold(
            uiState = HomeUiState(
                ringtones = listOf(
                    Ringtone("1", "Summer Breeze", "Sunny Artist", "", "", "0:30", "Nature")
                )
            ),
            onSearchClick = {},
            onSettingsClick = {},
            onRingtoneClick = {},
            onSetRingtone = {},
            onToggleFavorite = {},
            onCategorySelected = {}
        )
    }
}
