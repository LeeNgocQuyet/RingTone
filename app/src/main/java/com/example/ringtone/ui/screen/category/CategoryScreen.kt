package com.example.ringtone.ui.screen.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LimeGreen = Color(0xFFD4FF5B)
private val SoftPurple = Color(0xFFC5A3FF)

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onSettingsClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    CategoryScaffold(
        uiState = uiState,
        onSettingsClick = onSettingsClick,
        onCategoryClick = onCategoryClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScaffold(
    uiState: CategoryUiState,
    onSettingsClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category",
                            color = LimeGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp
                        )
                    }
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
        CategoryContent(
            modifier = Modifier.padding(innerPadding),
            categories = uiState.categories,
            onCategoryClick = onCategoryClick
        )
    }
}

@Composable
fun CategoryContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onCategoryClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category.id) }
            )
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .border(1.dp, SoftPurple, RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Vinyl Disc Image Placeholder with Hologram Effect
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF42A5F5),
                                Color(0xFF7E57C2),
                                Color(0xFF26C6DA),
                                Color(0xFF42A5F5)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Vinyl inner pattern
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = category.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            
            Text(
                text = "${category.itemCount} items",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CategoryPreview() {
    val dummyCategories = listOf(
        Category("1", "Top Ringtone", 12),
        Category("2", "Electronic", 25),
        Category("3", "Nature", 18),
        Category("4", "Classical", 10)
    )
    CategoryScaffold(
        uiState = CategoryUiState(categories = dummyCategories),
        onSettingsClick = {},
        onCategoryClick = {}
    )
}
