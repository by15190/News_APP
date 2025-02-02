package com.example.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.models.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(TypeConvertor::class)
abstract class
DATABASE : RoomDatabase() {

    abstract fun getDao(): DAO // fun for DAO

    companion object {
        @Volatile
        private var instance: DATABASE? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): DATABASE {
            // Return existing instance if it's not null
            return instance ?: synchronized(LOCK) { // Synchronize to ensure only one thread can access the database at a time
                // Check again if instance is null after acquiring the lock
                instance ?: createDatabase(context).also { //as soon as the createDatabase fun return the instance it is stored in the instance variable using also
                    instance = it // Assign the newly created instance to the variable
                }
            }
        }

        private fun createDatabase(context: Context): DATABASE {
            return Room.databaseBuilder(
                context.applicationContext,
                DATABASE::class.java,
                "favArticelDB" // Database name
            ).build()
        }
    }
}