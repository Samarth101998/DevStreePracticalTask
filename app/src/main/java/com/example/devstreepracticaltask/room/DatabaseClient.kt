package com.example.devstreepracticaltask.room

import android.content.Context
import androidx.room.Room


class DatabaseClient private constructor(context: Context) {

    val locationDatabase: LocationDatabase = Room.databaseBuilder(
        context, LocationDatabase::class.java,
        "location.db"
    ).allowMainThreadQueries()
        .build()


    companion object {

        private var instance: DatabaseClient? = null

        @Synchronized
        fun getDatabase(context: Context): DatabaseClient {
            if (instance == null) {
                instance = DatabaseClient(context)
            }
            return instance as DatabaseClient

        }
    }
}