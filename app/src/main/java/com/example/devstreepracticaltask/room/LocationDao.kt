package com.example.devstreepracticaltask.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationModel: LocationModel)

    @Query("Select * from location")
    fun getAllLocations(): List<LocationModel>

    @Query("Update location set Name = :name, Address = :address, Latitude = :latitude, Longitude = :longitude Where id =:id")
    fun update(name: String, address: String, latitude: Double, longitude: Double, id: Int)

    @Query("Delete from location Where id = :id")
    fun delete(id: Int)

    @Query("Select * from location where PrimaryLocation=:pri")
    fun getPrimary(pri: Boolean): LocationModel
}