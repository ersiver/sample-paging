package com.example.android.codelabs.paging.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.codelabs.paging.model.Repo

@Database(entities = [Repo::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun reposDao(): RepoDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile

        private var INSTANCE: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase {
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context,
                            RepoDatabase::class.java,
                            "Github.db")
                            .build()
                    INSTANCE = instance

                }
                return instance
            }

        }
    }
}