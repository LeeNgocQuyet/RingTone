package com.example.ringtone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ringtone.data.player.RingtonePlayer
import com.example.ringtone.data.repository.FakeRingtoneRepository
import com.example.ringtone.domain.repository.RingtoneRepository
import com.example.ringtone.ui.home.HomeScreen
import com.example.ringtone.ui.home.HomeViewModel
import com.example.ringtone.ui.navigation.Screen
import com.example.ringtone.ui.navigation.bottomNavItems
import com.example.ringtone.ui.profile.ProfileScreen
import com.example.ringtone.ui.search.SearchScreen
import com.example.ringtone.ui.search.SearchViewModel
import com.example.ringtone.ui.theme.RingToneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Khởi tạo repository ở cấp Activity (hoặc Application)
        val repository = FakeRingtoneRepository()

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            
            // Sử dụng Factory để truyền repository vào ViewModel
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return when {
                        modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
                        modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository) as T
                        else -> throw IllegalArgumentException("Unknown ViewModel class")
                    }
                }
            }

            // Sử dụng hàm viewModel() để đảm bảo ViewModel tồn tại qua quá trình xoay màn hình
            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            val searchViewModel: SearchViewModel = viewModel(factory = factory)

            val player = remember { RingtonePlayer(context) }
            
            DisposableEffect(Unit) {
                onDispose {
                    player.release()
                }
            }

            RingToneTheme {
                MainApp(
                    homeViewModel = homeViewModel,
                    searchViewModel = searchViewModel,
                    onPlayClick = { ringtone -> player.play(ringtone.audioUrl) }
                )
            }
        }
    }
}

@Composable
fun MainApp(
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    onPlayClick: (com.example.ringtone.domain.model.Ringtone) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                HomeScreen(homeViewModel, onPlayClick) 
            }
            composable(Screen.Search.route) { 
                SearchScreen(searchViewModel, onPlayClick) 
            }
            composable(Screen.Profile.route) { 
                ProfileScreen() 
            }
        }
    }
}
