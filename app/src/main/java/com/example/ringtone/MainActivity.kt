package com.example.ringtone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ringtone.data.repository.FakeRingtoneRepository
import com.example.ringtone.player.RingtonePlayer
import com.example.ringtone.ui.navigation.AppNavGraph
import com.example.ringtone.ui.navigation.Screen
import com.example.ringtone.ui.screen.home.HomeViewModel
import com.example.ringtone.ui.screen.list.RingtoneListViewModel
import com.example.ringtone.ui.screen.search.SearchViewModel
import com.example.ringtone.ui.theme.RingToneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val repository = FakeRingtoneRepository()

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            
            val factory = remember {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return when {
                            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
                            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository) as T
                            modelClass.isAssignableFrom(RingtoneListViewModel::class.java) -> RingtoneListViewModel(repository) as T
                            // TODO: Thêm DownloadViewModel, CategoryViewModel... khi bạn tạo file
                            else -> throw IllegalArgumentException("Unknown ViewModel class")
                        }
                    }
                }
            }

            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            val searchViewModel: SearchViewModel = viewModel(factory = factory)
            val listViewModel: RingtoneListViewModel = viewModel(factory = factory)

            val player = remember { RingtonePlayer(context) }
            
            DisposableEffect(Unit) {
                onDispose {
                    player.release()
                }
            }

            val backstack = rememberSaveableMutableStateListOf<Screen>(Screen.Home)

            RingToneTheme {
                Scaffold(
                    bottomBar = {
                        // Hiển thị BottomBar cho 4 màn hình chính
                        if (backstack.last() is Screen.Home || backstack.last() is Screen.Download || 
                            backstack.last() is Screen.Category || backstack.last() is Screen.Playlist) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                    label = { Text("Home") },
                                    selected = backstack.last() is Screen.Home,
                                    onClick = { 
                                        backstack.clear()
                                        backstack.add(Screen.Home) 
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Download, contentDescription = null) },
                                    label = { Text("Download") },
                                    selected = backstack.last() is Screen.Download,
                                    onClick = {
                                        backstack.clear()
                                        backstack.add(Screen.Download)
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Category, contentDescription = null) },
                                    label = { Text("Category") },
                                    selected = backstack.last() is Screen.Category,
                                    onClick = {
                                        backstack.clear()
                                        backstack.add(Screen.Category)
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                                    label = { Text("Playlist") },
                                    selected = backstack.last() is Screen.Playlist,
                                    onClick = {
                                        backstack.clear()
                                        backstack.add(Screen.Playlist)
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavGraph(
                            backStack = backstack,
                            homeViewModel = homeViewModel,
                            searchViewModel = searchViewModel,
                            downloadViewModel = null, // Update when ViewModel is ready
                            categoryViewModel = null,
                            playlistViewModel = null,
                            audioViewModel = null,
                            audioInfoViewModel = null,
                            listViewModel = listViewModel,
                            onNavigate = { screen -> backstack.add(screen) },
                            onPopBack = { if (backstack.size > 1) backstack.removeAt(backstack.size - 1) },
                            onPlayClick = { ringtone -> player.play(ringtone.audioUrl) }
                        )
                    }
                }
            }

            BackHandler(enabled = backstack.size > 1) {
                backstack.removeAt(backstack.size - 1)
            }
        }
    }
}

@Composable
fun <T> rememberSaveableMutableStateListOf(vararg elements: T): MutableList<T> {
    val saver = remember {
        androidx.compose.runtime.saveable.listSaver<MutableList<T>, T>(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    }
    return rememberSaveable(saver = saver) {
        elements.toList().toMutableStateList()
    }
}
