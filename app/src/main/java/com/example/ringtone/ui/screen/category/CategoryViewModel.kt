package com.example.ringtone.ui.screen.category

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Category(
    val id: String,
    val title: String,
    val itemCount: Int,
    val imageUrl: String = ""
)

data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false
)

class CategoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        // Dummy data for categories
        _uiState.value = _uiState.value.copy(
            categories = listOf(
                Category("1", "Top Ringtone", 12),
                Category("2", "Electronic", 25),
                Category("3", "Nature", 18),
                Category("4", "Classical", 10),
                Category("5", "TikTok Viral", 30),
                Category("6", "Lofi Beats", 22)
            )
        )
    }
}
