package com.example.ringtone.data.repository

import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.domain.repository.RingtoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRingtoneRepository : RingtoneRepository {
    private val fakeRingtones = listOf(
        Ringtone("1", "Summer Breeze", "Sunny Artist", "", "", "0:30", "Nature"),
        Ringtone("2", "Digital Horizon", "Tech Beats", "", "", "0:25", "Electronic"),
        Ringtone("3", "Morning Dew", "Calm Master", "", "", "0:45", "Classical")
    )

    override fun getRingtones(): Flow<List<Ringtone>> = flowOf(fakeRingtones)

    override fun searchRingtones(query: String): Flow<List<Ringtone>> {
        return flowOf(fakeRingtones.filter { it.title.contains(query, ignoreCase = true) })
    }
}
