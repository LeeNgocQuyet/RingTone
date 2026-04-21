package com.example.ringtone.ui.search

import androidx.lifecycle.SavedStateHandle
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
    val query: String = "",
    val isLoading: Boolean = false
)

class SearchViewModel(private val repository: RingtoneRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onSearchQueryChanged(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
        search(newQuery)
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

}
