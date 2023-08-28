package com.example.devstreepracticaltask.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "location")
@Parcelize
data class LocationModel(
    @ColumnInfo(name = "Name") var name: String,
    @ColumnInfo(name = "Address") var address: String,
    @ColumnInfo(name = "Latitude") var latitude: Double,
    @ColumnInfo(name = "Longitude") var longitude: Double,
    @ColumnInfo(name = "PrimaryLocation") var primary: Boolean,
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}