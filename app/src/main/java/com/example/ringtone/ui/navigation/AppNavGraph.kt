package com.example.ringtone.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.ui.screen.category.CategoryScreen
import com.example.ringtone.ui.screen.category.CategoryViewModel
import com.example.ringtone.ui.screen.download.DownloadScreen
import com.example.ringtone.ui.screen.download.DownloadViewModel
import com.example.ringtone.ui.screen.home.HomeScreen
import com.example.ringtone.ui.screen.home.HomeViewModel
import com.example.ringtone.ui.screen.list.RingtoneListScreen
import com.example.ringtone.ui.screen.list.RingtoneListViewModel
import com.example.ringtone.ui.screen.playlist.PlaylistScreen
import com.example.ringtone.ui.screen.playlist.PlaylistViewModel
import com.example.ringtone.ui.screen.search.SearchScreen
import com.example.ringtone.ui.screen.search.SearchViewModel

@Composable
fun AppNavGraph(
    backStack: List<Screen>,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    downloadViewModel: DownloadViewModel,
    categoryViewModel: CategoryViewModel,
    playlistViewModel: PlaylistViewModel,
    audioViewModel: Any?, // Cần cập nhật khi có AudioViewModel
    audioInfoViewModel: Any?, // Cần cập nhật khi có AudioInfoViewModel
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
                    onSettingsClick = { /* Navigate to Settings or show bottom sheet */ },
                    onSetRingtone = { /* Handle set ringtone logic */ },
                    onToggleFavorite = { ringtoneId -> homeViewModel.toggleFavorite(ringtoneId) }
                )
            }
            Screen.Download -> NavEntry(Screen.Download) {
                DownloadScreen(
                    viewModel = downloadViewModel,
                    onSettingsClick = { /* Handle settings */ },
                    onMoreClick = { onNavigate(Screen.List("download")) },
                    onRingtoneClick = { id -> onNavigate(Screen.Audio(id)) }
                )
            }
            Screen.Category -> NavEntry(Screen.Category) {
                CategoryScreen(
                    viewModel = categoryViewModel,
                    onSettingsClick = { /* Handle settings */ },
                    onCategoryClick = { id -> onNavigate(Screen.List("category", id)) }
                )
            }
            Screen.Playlist -> NavEntry(Screen.Playlist) {
                PlaylistScreen(
                    viewModel = playlistViewModel,
                    onSettingsClick = { /* Handle settings */ },
                    onDownloadAudioClick = { onNavigate(Screen.Download) },
                    onRingtoneClick = { id -> onNavigate(Screen.Audio(id)) },
                    onSetRingtone = { /* Handle set ringtone logic */ },
                    onToggleFavorite = { ringtoneId -> playlistViewModel.toggleFavorite(ringtoneId) }
                )
            }
            Screen.Search -> NavEntry(Screen.Search) {
                SearchScreen(
                    viewModel = searchViewModel,
                    onBackClick = onPopBack,
                    onRingtoneClick = { id -> onNavigate(Screen.Audio(id)) }
                )
            }
            is Screen.Audio -> NavEntry(screen) {
                // Sẽ thay bằng AudioScreen khi bạn tạo file
                Text("Audio Playing: ${screen.ringtoneId}")
            }
            is Screen.AudioInfo -> NavEntry(screen) {
                // Sẽ thay bằng AudioInfoScreen khi bạn tạo file
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
