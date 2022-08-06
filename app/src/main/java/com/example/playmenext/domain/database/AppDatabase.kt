package com.example.playmenext.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playmenext.domain.PieceToPractice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [PieceToPractice::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pieceDao() : PieceToPracticeDao

    companion object {
        // Singleton to avoid multiple access to the same db
        @Volatile
        private var _instance : AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return _instance ?: synchronized(this) {
                val newInst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                _instance = newInst
                // return newInst
                newInst
            }
        }
    }

    private class AppDatabaseCallback(private val _scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            _instance?.let{ database ->
                _scope.launch { populateDatabase(database.pieceDao()) }
            }
        }

        suspend fun populateDatabase(pieceDao : PieceToPracticeDao) {
            pieceDao.deleteAll()

            // add samples
            val test = PieceToPractice(title = "Moonlight Sonata", subTitle = "1st Movement",
                composer = "L. v. Beethoven", practicePriority = 1.0)
            pieceDao.insert(test)
        }
    }
}