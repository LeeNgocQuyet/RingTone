package com.example.ringtone.domain.model

data class Ringtone(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val audioUrl: String = "",
    val imageUrl: String = "",
    val duration: String = "",
    val category: String = ""
)