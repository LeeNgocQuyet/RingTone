package com.example.ringtone.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.ui.screen.home.HomeScreen
import com.example.ringtone.ui.screen.home.HomeViewModel
import com.example.ringtone.ui.screen.list.RingtoneListScreen
import com.example.ringtone.ui.screen.list.RingtoneListViewModel
import com.example.ringtone.ui.screen.search.SearchScreen
import com.example.ringtone.ui.screen.search.SearchViewModel

@Composable
fun AppNavGraph(
    backStack: List<Screen>,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    downloadViewModel: Any?,
    categoryViewModel: Any?,
    playlistViewModel: Any?,
    audioViewModel: Any?,
    audioInfoViewModel: Any?,
    listViewModel: RingtoneListViewModel,
    onNavigate: (Screen) -> Unit,
    onPopBack: () -> Unit,
    onPlayClick: (Ringtone) -> Unit
) {
    NavDisplay(
        backStack = backStack,
        onBack = onPopBack,
    ) { screen: Screen ->
        when (screen) {
            Screen.Home -> NavEntry(Screen.Home) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onSearchClick = { onNavigate(Screen.Search) },
                    onRingtoneClick = { id -> onNavigate(Screen.Audio(id)) },
                    onSettingsClick = { },
                    onSetRingtone = { },
                    onToggleFavorite = { },
                )
            }
            Screen.Download -> NavEntry(Screen.Download) {
                Text("Download Screen")
            }
            Screen.Category -> NavEntry(Screen.Category) {
                Text("Category Screen")
            }
            Screen.Playlist -> NavEntry(Screen.Playlist) {
                Text("Playlist Screen")
            }
            Screen.Search -> NavEntry(Screen.Search) {
                SearchScreen(
                    viewModel = searchViewModel,
                    onPlayClick = onPlayClick
                )
            }
            is Screen.Audio -> NavEntry(screen) {
                Text("Audio Playing: ${screen.ringtoneId}")
            }
            is Screen.AudioInfo -> NavEntry(screen) {
                Text("Audio Info for: ${screen.ringtoneId}")
            }
            is Screen.List -> NavEntry(screen) {
                RingtoneListScreen(
                    type = screen.type,
                    viewModel = listViewModel,
                    onBackClick = onPopBack,
                    onPlayClick = onPlayClick
                )
            }
        }
    }
}
