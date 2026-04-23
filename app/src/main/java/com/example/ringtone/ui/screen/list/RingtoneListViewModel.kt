package com.example.ringtone.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.domain.repository.RingtoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RingtoneListUiState(
    val title: String = "",
    val ringtones: List<Ringtone> = emptyList(),
    val isLoading: Boolean = false
)

class RingtoneListViewModel(private val repository: RingtoneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RingtoneListUiState())
    val uiState: StateFlow<RingtoneListUiState> = _uiState.asStateFlow()

    fun loadRingtones(type: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, title = type.replaceFirstChar { it.uppercase() })
            val flow = when (type.lowercase()) {
                "favorites" -> repository.getFavorites()
                "download" -> repository.getDownloads()
                else -> repository.getRingtones()
            }
            flow.collect { list ->
                _uiState.value = _uiState.value.copy(ringtones = list, isLoading = false)
            }
        }
    }
}
