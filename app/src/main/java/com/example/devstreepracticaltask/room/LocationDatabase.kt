package com.example.devstreepracticaltask.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationModel::class], version = 1)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}