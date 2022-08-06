package com.example.playmenext.domain

import androidx.annotation.WorkerThread
import com.example.playmenext.domain.database.PieceToPracticeDao

class PiecesRepository(private val _pieceDao: PieceToPracticeDao) {
    val allPieces = _pieceDao.getAllPieces()

    @WorkerThread
    suspend fun insert(piece: PieceToPractice) {
        _pieceDao.insert(piece)
    }

    @WorkerThread
    suspend fun delete(piece: PieceToPractice) {
        _pieceDao.delete(piece)
    }
}