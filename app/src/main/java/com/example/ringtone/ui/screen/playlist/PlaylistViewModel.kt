package com.example.ringtone.ui.screen.playlist

import androidx.lifecycle.ViewModel
import com.example.ringtone.domain.model.Ringtone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PlaylistTab {
    Downloads, Favorites
}

data class PlaylistUiState(
    val selectedTab: PlaylistTab = PlaylistTab.Downloads,
    val items: List<Ringtone> = emptyList(),
    val isLoading: Boolean = false
)

class PlaylistViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    fun onTabSelected(tab: PlaylistTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
        // In a real app, load data for the specific tab
        loadDataForTab(tab)
    }

    private fun loadDataForTab(tab: PlaylistTab) {
        // For now, we keep it empty to match the "Empty here" requirement
        _uiState.value = _uiState.value.copy(items = emptyList())
    }
}
