package com.example.devstreepracticaltask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.devstreepracticaltask.adapter.LocationAdapter
import com.example.devstreepracticaltask.databinding.ActivityLocationsBinding
import com.example.devstreepracticaltask.databinding.ActivityMapsBinding
import com.example.devstreepracticaltask.room.DatabaseClient
import com.example.devstreepracticaltask.room.LocationModel
import com.example.devstreepracticaltask.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.Collections

class LocationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLocationsBinding
    private val viewModel by viewModels<LocationViewModel>()
    private var locationList = ArrayList<LocationModel>()
    private lateinit var adapter: LocationAdapter
    var sortBy = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)
        binding = ActivityLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = LocationAdapter(applicationContext, locationList, {
            startActivity(
                Intent(
                    this@LocationsActivity,
                    MapsActivity::class.java
                ).putExtra("id", it.id).putExtra("lat", it.latitude).putExtra("lng", it.longitude)
            )
        }) {
            DatabaseClient.getDatabase(applicationContext).locationDatabase.locationDao().delete(it)
            viewModel.getLocations(applicationContext)
        }
        binding.recyclerViewLocation.adapter = adapter

        viewModel.getLocations(applicationContext)
        viewModel.locationList.observe(this) {
            if (it != null) {
                locationList.clear()
                locationList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        binding.btnAddLocation.setOnClickListener {
            startActivity(Intent(this@LocationsActivity, MapsActivity::class.java))
        }

        binding.tvAs.setOnClickListener {
            sortList()
            sortBy = "as"
            adapter.notifyDataSetChanged()
        }

        binding.tvDe.setOnClickListener {
            sortList()
            sortBy = "de"
            locationList.reverse()
            adapter.notifyDataSetChanged()
        }

        binding.ivRoute.setOnClickListener {
            if (locationList.isNotEmpty()) {
                if (locationList.size >= 2) {
                    if (sortBy.isBlank()) {
                        sortList()
                    }
                    startActivity(
                        Intent(
                            this@LocationsActivity,
                            MapsActivity::class.java
                        ).putParcelableArrayListExtra("route", locationList)
                    )
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Minimum 2 location is required",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(applicationContext, "Add locations", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun sortList() {
        val model =
            DatabaseClient.getDatabase(applicationContext).locationDatabase.locationDao()
                .getPrimary(true)

        val latLng = LatLng(model.latitude, model.longitude)

        Collections.sort(locationList, SortPlaces(latLng))
    }
}