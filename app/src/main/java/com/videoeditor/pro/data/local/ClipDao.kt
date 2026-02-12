package com.videoeditor.pro.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {

    @Query("SELECT * FROM clips")
    fun getAllClipsFlow(): Flow<List<DbClip>>

    @Query("SELECT * FROM clips")
    suspend fun getAllClips(): List<DbClip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClip(clip: DbClip)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClips(clips: List<DbClip>)

    @Update
    suspend fun updateClip(clip: DbClip)

    @Query("DELETE FROM clips WHERE id = :clipId")
    suspend fun deleteClipById(clipId: String)

    @Query("DELETE FROM clips")
    suspend fun clearAll()
}

