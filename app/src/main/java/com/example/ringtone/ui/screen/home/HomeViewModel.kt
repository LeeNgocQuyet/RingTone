package com.example.ringtone.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.domain.repository.RingtoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val ringtones: List<Ringtone> = emptyList(),
    val categories: List<String> = listOf("All", "Nature", "Electronic", "Classical", "Pop", "Rock"),
    val selectedCategory: String = "All",
    val favoriteIds: Set<String> = emptySet(),
    val isLoading: Boolean = false
)

class HomeViewModel(private val repository: RingtoneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadRingtones()
    }

    private fun loadRingtones() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getRingtones().collect { ringtones ->
                _uiState.value = _uiState.value.copy(
                    ringtones = ringtones,
                    isLoading = false
                )
            }
        }
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
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
