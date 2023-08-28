package com.example.devstreepracticaltask

import com.example.devstreepracticaltask.room.LocationModel
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng


class SortPlaces(var currentLoc: LatLng) : Comparator<LocationModel> {
    override fun compare(place1: LocationModel, place2: LocationModel): Int {
        val lat1: Double = place1.latitude
        val lon1: Double = place1.longitude
        val lat2: Double = place2.latitude
        val lon2: Double = place2.longitude
        val distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1)
        val distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2)
        return (distanceToPlace1 - distanceToPlace2).toInt()
    }

    fun distance(fromLat: Double, fromLon: Double, toLat: Double, toLon: Double): Double {
        val radius = 6378137.0 // approximate Earth radius, *in meters*
        val deltaLat = toLat - fromLat
        val deltaLon = toLon - fromLon
        val angle = 2 * Math.asin(
            Math.sqrt(
                Math.pow(
                    Math.sin(deltaLat / 2),
                    2.0
                ) + Math.cos(fromLat) * Math.cos(toLat) * Math.pow(
                    Math.sin(deltaLon / 2), 2.0
                )
            )
        )
        return radius * angle
    }
}