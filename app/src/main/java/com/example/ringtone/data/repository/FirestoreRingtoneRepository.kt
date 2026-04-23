package com.example.ringtone.data.repository

import com.example.ringtone.domain.model.Ringtone
import com.example.ringtone.domain.repository.RingtoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Skeleton for Firebase integration.
 */
class FirestoreRingtoneRepository : RingtoneRepository {

    override fun getRingtones(): Flow<List<Ringtone>> = flow {
        emit(emptyList())
    }

    override fun searchRingtones(query: String): Flow<List<Ringtone>> = flow {
        emit(emptyList())
    }

    override fun getFavorites(): Flow<List<Ringtone>> = flow {
        emit(emptyList())
    }

    override fun getDownloads(): Flow<List<Ringtone>> = flow {
        emit(emptyList())
    }
}
