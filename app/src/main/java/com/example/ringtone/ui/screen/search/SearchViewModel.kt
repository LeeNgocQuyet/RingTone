package com.example.ringtone.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.domain.repository.RingtoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val results: List<Ringtone> = emptyList(),
    val suggestions: List<Ringtone> = emptyList(),
    val query: String = "",
    val history: List<String> = listOf("Lo-fi", "Chill", "Techno", "Drake", "Anime Opening"),
    val favoriteIds: Set<String> = emptySet(),
    val isLoading: Boolean = false
)

class SearchViewModel(private val repository: RingtoneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadSuggestions()
    }

    private fun loadSuggestions() {
        viewModelScope.launch {
            repository.getRingtones().collect { ringtones ->
                _uiState.value = _uiState.value.copy(suggestions = ringtones)
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
        if (newQuery.isNotEmpty()) {
            search(newQuery)
        } else {
            _uiState.value = _uiState.value.copy(results = emptyList(), isLoading = false)
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.searchRingtones(query).collect { results ->
                _uiState.value = _uiState.value.copy(
                    results = results,
                    isLoading = false
                )
            }
        }
    }

    fun removeHistoryItem(item: String) {
        _uiState.value = _uiState.value.copy(
            history = _uiState.value.history.filter { it != item }
        )
    }

    fun toggleFavorite(ringtoneId: String) {
        val currentFavorites = _uiState.value.favoriteIds
        val newFavorites = if (currentFavorites.contains(ringtoneId)) {
            currentFavorites - ringtoneId
        } else {
            currentFavorites + ringtoneId
        }
        _uiState.value = _uiState.value.copy(favoriteIds = newFavorites)
    }
}
