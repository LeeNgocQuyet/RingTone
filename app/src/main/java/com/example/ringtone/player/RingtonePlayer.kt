package com.example.ringtone.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class RingtonePlayer(context: Context) {
    private val exoPlayer = ExoPlayer.Builder(context).build()

    fun play(url: String) {
        if (url.isEmpty()) return
        
        try {
            if (exoPlayer.isPlaying) {
                exoPlayer.stop()
            }
            
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        exoPlayer.stop()
    }

    fun release() {
        exoPlayer.release()
    }
}
