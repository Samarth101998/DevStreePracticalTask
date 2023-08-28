package com.example.devstreepracticaltask.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.devstreepracticaltask.room.DatabaseClient
import com.example.devstreepracticaltask.room.LocationModel
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class LocationViewModel() : ViewModel() {

    val locationList = MutableLiveData<List<LocationModel>>()

    fun getLocations(context: Context) {
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val result =
                DatabaseClient.getDatabase(context).locationDatabase.locationDao().getAllLocations()
            locationList.postValue(result)
        }
    }
}