package com.example.ringtone.ui.screen.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.domain.repository.RingtoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DownloadUiState(
    val linkUrl: String = "",
    val downloadHistory: List<Ringtone> = emptyList(),
    val isDownloading: Boolean = false,
    val favoriteIds: Set<String> = emptySet()
)

class DownloadViewModel(private val repository: RingtoneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DownloadUiState())
    val uiState: StateFlow<DownloadUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            repository.getDownloads().collect { history ->
                _uiState.value = _uiState.value.copy(downloadHistory = history)
            }
        }
    }

    fun onLinkChanged(newLink: String) {
        _uiState.value = _uiState.value.copy(linkUrl = newLink)
    }

    fun downloadAudio() {
        if (_uiState.value.linkUrl.isBlank()) return
        // Trigger download logic
    }

    fun toggleFavorite(id: String) {
        val current = _uiState.value.favoriteIds
        val next = if (current.contains(id)) current - id else current + id
        _uiState.value = _uiState.value.copy(favoriteIds = next)
    }
}
