package com.example.ringtone.data.repository

import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.domain.repository.RingtoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRingtoneRepository : RingtoneRepository {
    private val allRingtones = listOf(
        Ringtone(
            id = "1",
            title = "Tik Viral Hit 2024",
            artist = "Trending Music",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            imageUrl = "https://picsum.photos/400/400?random=1",
            duration = "00:30",
            category = "TikTok"
        ),
        Ringtone(
            id = "2",
            title = "Summer Breeze",
            artist = "Sunny Artist",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            imageUrl = "https://picsum.photos/400/400?random=2",
            duration = "00:45",
            category = "Nature"
        ),
        Ringtone(
            id = "3",
            title = "Digital Horizon",
            artist = "Tech Beats",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            imageUrl = "https://picsum.photos/400/400?random=3",
            duration = "00:25",
            category = "Electronic"
        ),
        Ringtone(
            id = "4",
            title = "Morning Dew",
            artist = "Calm Master",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            imageUrl = "https://picsum.photos/400/400?random=4",
            duration = "00:40",
            category = "Classical"
        ),
        Ringtone(
            id = "5",
            title = "Chill Beat Lofi",
            artist = "Lofi Girl",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
            imageUrl = "https://picsum.photos/400/400?random=5",
            duration = "01:20",
            category = "Lofi"
        ),
        Ringtone(
            id = "6",
            title = "Dramatic Impact",
            artist = "SFX Master",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3",
            imageUrl = "https://picsum.photos/400/400?random=6",
            duration = "00:15",
            category = "SFX"
        )
    )

    override fun getRingtones(): Flow<List<Ringtone>> = flowOf(allRingtones)

    override fun searchRingtones(query: String): Flow<List<Ringtone>> {
        return flowOf(allRingtones.filter { 
            it.title.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)
        })
    }

    override fun getFavorites(): Flow<List<Ringtone>> = flowOf(allRingtones.take(3))
    
    override fun getDownloads(): Flow<List<Ringtone>> = flowOf(allRingtones.filter { it.category == "TikTok" || it.category == "Lofi" })
}
