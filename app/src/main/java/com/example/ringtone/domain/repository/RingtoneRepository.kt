package com.example.ringtone.domain.repository

import com.example.ringtone.domain.model.Ringtone
import kotlinx.coroutines.flow.Flow

interface RingtoneRepository {
    fun getRingtones(): Flow<List<Ringtone>>
    fun searchRingtones(query: String): Flow<List<Ringtone>>
}
