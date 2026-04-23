package com.example.ringtone.ui.screen.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.domain.repository.RingtoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class PlaylistTab {
    Downloads, Favorites
}

data class PlaylistUiState(
    val selectedTab: PlaylistTab = PlaylistTab.Downloads,
    val items: List<Ringtone> = emptyList(),
    val isLoading: Boolean = false,
    val favoriteIds: Set<String> = emptySet()
)

class PlaylistViewModel(private val repository: RingtoneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    init {
        loadFavoriteIds()
        loadDataForTab(PlaylistTab.Downloads)
    }

    private fun loadFavoriteIds() {
        viewModelScope.launch {
            repository.getFavorites().collect { favorites ->
                _uiState.value = _uiState.value.copy(
                    favoriteIds = favorites.map { it.id }.toSet()
                )
            }
        }
    }

    fun onTabSelected(tab: PlaylistTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
        loadDataForTab(tab)
    }

    private fun loadDataForTab(tab: PlaylistTab) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val flow = when (tab) {
                PlaylistTab.Downloads -> repository.getDownloads()
                PlaylistTab.Favorites -> repository.getFavorites()
            }
            flow.collect { list ->
                _uiState.value = _uiState.value.copy(items = list, isLoading = false)
            }
        }
    }

    fun toggleFavorite(ringtoneId: String) {
        val currentFavorites = _uiState.value.favoriteIds
        val newFavorites = if (currentFavorites.contains(ringtoneId)) {
            currentFavorites - ringtoneId
        } else {
            currentFavorites + ringtoneId
        }
        _uiState.value = _uiState.value.copy(favoriteIds = newFavorites)
        
        if (_uiState.value.selectedTab == PlaylistTab.Favorites) {
            loadDataForTab(PlaylistTab.Favorites)
        }
    }
}
