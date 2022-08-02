package com.example.playmenext.domain.database

import androidx.room.*
import com.example.playmenext.domain.PieceToPractice
import com.example.playmenext.domain.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface PieceToPracticeDao {
    @Query("SELECT * FROM $TABLE_NAME ORDER BY title ASC")
    fun getAllPieces() : Flow<List<PieceToPractice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(piece : PieceToPractice)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(piece : PieceToPractice)
}