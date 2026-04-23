package com.example.ringtone.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : Parcelable { // Thêm : Parcelable
    @Parcelize
    @Serializable
    data object Home : Screen

    @Parcelize
    @Serializable
    data object Download : Screen

    @Parcelize
    @Serializable
    data object Category : Screen

    @Parcelize
    @Serializable
    data object Playlist : Screen

    @Parcelize
    @Serializable
    data object Search : Screen

    @Parcelize
    @Serializable
    data class Audio(val ringtoneId: String) : Screen

    @Parcelize
    @Serializable
    data class AudioInfo(val ringtoneId: String) : Screen

    @Parcelize
    @Serializable
    data class List(val type: String, val id: String? = null) : Screen
}