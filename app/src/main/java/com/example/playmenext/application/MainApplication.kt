package com.example.playmenext.application

import android.app.Application
import com.example.playmenext.domain.PiecesRepository
import com.example.playmenext.domain.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {
    // scope ends together with the process
    var appScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { AppDatabase.getDatabase(this, appScope) }
    val repository by lazy { PiecesRepository(database.pieceDao()) }
}