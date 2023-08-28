package com.example.devstreepracticaltask

import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.devstreepracticaltask.databinding.ActivityMapsBinding
import com.example.devstreepracticaltask.room.DatabaseClient
import com.example.devstreepracticaltask.room.LocationModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    val rectOptions: PolylineOptions = PolylineOptions()
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    var locationModel: LocationModel? = null
    var locationList = ArrayList<LocationModel>()
    var id = 0;
    var latitude = 23.63936
    var longitude = 68.14712

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyBSNyp6GQnnKlrMr7hD2HGiyF365tFlK5U");
        }

        autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocompleteFragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.name)

                val location: String = place.name!!.toString()

                var addressList: List<Address>? = null

                if (location.isNotEmpty()) {
                    val geocoder = Geocoder(this@MapsActivity)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val address = addressList!![0]
                    Log.d("<>", "Address: ${addressList[0].getAddressLine(0)}")
                    Log.d("<>", "Locality: ${addressList[0].locality}")
                    Log.d("<>", "Country Name: ${addressList[0].countryName}")

                    val latLng = LatLng(address.latitude, address.longitude)

                    mMap.addMarker(MarkerOptions().position(latLng).title(location))

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

                    binding.addLocation.visibility = View.VISIBLE

                    locationModel = LocationModel(
                        place.name!!.toString(),
                        addressList[0].getAddressLine(0),
                        address.latitude,
                        address.longitude,
                        false
                    )
                }
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: $status")
            }
        })
        mapFragment.getMapAsync(this)

        if (intent != null) {
            if (intent.hasExtra("id")) {
                id = intent.getIntExtra("id", 0)
                latitude = intent.getDoubleExtra("lat", 0.0)
                longitude = intent.getDoubleExtra("lng", 0.0)
                binding.updateLocation.visibility = View.VISIBLE
            }

            if (intent.hasExtra("route")) {
                locationList = intent.getParcelableArrayListExtra("route")!!
            }
        }

        binding.btnSave.setOnClickListener {
            if (DatabaseClient.getDatabase(applicationContext).locationDatabase.locationDao()
                    .getAllLocations().isEmpty()
            ) {
                locationModel!!.primary = true
            }
            DatabaseClient.getDatabase(applicationContext).locationDatabase.locationDao().insert(
                locationModel!!
            )
            startActivity(Intent(this@MapsActivity, LocationsActivity::class.java))
        }

        binding.btnUpdate.setOnClickListener {
            if (locationModel != null) {
                DatabaseClient.getDatabase(applicationContext).locationDatabase.locationDao()
                    .update(
                        locationModel!!.name,
                        locationModel!!.address,
                        locationModel!!.latitude,
                        locationModel!!.longitude,
                        id
                    )
            }
            startActivity(Intent(this@MapsActivity, LocationsActivity::class.java))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("TAG", "onCreate: id: ${id}, lat: ${latitude}, lng: ${longitude}")
        val latLng = LatLng(latitude, longitude)
        if (id != 0) {
            mMap.addMarker(MarkerOptions().position(latLng))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4f))
        }

        Log.d("TAG", "onMapReady: rectOptions: ${locationList.isNotEmpty()}")
        if(locationList.isNotEmpty()){
            rectOptions.color(Color.argb(255, 85, 166, 27))
            locationList.forEach {
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(it.latitude, it.longitude))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10f))

                markerOptions.title("" + it.name)
                mMap.addMarker(markerOptions)

                rectOptions.add(LatLng(it.latitude, it.longitude))
            }
            mMap.addPolyline(rectOptions)
        }
    }
}