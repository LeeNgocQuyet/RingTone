package com.example.ringtone.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen
    
    @Serializable
    data object Download : Screen
    
    @Serializable
    data object Category : Screen
    
    @Serializable
    data object Playlist : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data class Audio(val ringtoneId: String) : Screen

    @Serializable
    data class AudioInfo(val ringtoneId: String) : Screen

    @Serializable
    data class List(val type: String, val id: String? = null) : Screen
}
