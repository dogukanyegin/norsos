package com.example.norsos.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val altitude: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
) {
    val googleMapsUrl: String
        get() = String.format(Locale.US, "https://maps.google.com/?q=%.6f,%.6f", latitude, longitude)

    val formattedCoordinates: String
        get() = String.format(Locale.US, "%.5f° N, %.5f° E (±%.0fm)", latitude, longitude, accuracy)
}

/**
 * Native GPS and Network Location Tracker using Android Framework LocationManager.
 * Does not require external Google Play Services binaries, works 100% offline.
 */
class LocationTracker(private val context: Context) : LocationListener {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

    private val _currentLocation = MutableStateFlow<LocationData?>(null)
    val currentLocation: StateFlow<LocationData?> = _currentLocation.asStateFlow()

    private val _isGpsEnabled = MutableStateFlow(false)
    val isGpsEnabled: StateFlow<Boolean> = _isGpsEnabled.asStateFlow()

    init {
        checkProviders()
        readLastKnownLocation()
    }

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    fun checkProviders() {
        try {
            val gps = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
            val net = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false
            _isGpsEnabled.value = gps || net
        } catch (e: Exception) {
            Log.w("LocationTracker", "Error checking provider status", e)
        }
    }

    fun readLastKnownLocation() {
        if (!hasLocationPermission()) return
        checkProviders()
        try {
            val providers = listOfNotNull(
                LocationManager.GPS_PROVIDER,
                LocationManager.NETWORK_PROVIDER,
                LocationManager.PASSIVE_PROVIDER
            )
            var bestLocation: Location? = null
            for (p in providers) {
                if (locationManager?.isProviderEnabled(p) == true) {
                    val loc = locationManager.getLastKnownLocation(p)
                    if (loc != null) {
                        if (bestLocation == null || loc.time > bestLocation.time) {
                            bestLocation = loc
                        }
                    }
                }
            }
            bestLocation?.let { updateLocationData(it) }
        } catch (e: SecurityException) {
            Log.w("LocationTracker", "Security exception getting last known location", e)
        }
    }

    fun startUpdates() {
        if (!hasLocationPermission()) return
        checkProviders()
        readLastKnownLocation()
        try {
            if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    3000L,
                    3f,
                    this
                )
            }
            if (locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000L,
                    5f,
                    this
                )
            }
        } catch (e: SecurityException) {
            Log.w("LocationTracker", "Security exception requesting location updates", e)
        }
    }

    fun stopUpdates() {
        try {
            locationManager?.removeUpdates(this)
        } catch (_: Exception) {}
    }

    private fun updateLocationData(loc: Location) {
        _currentLocation.value = LocationData(
            latitude = loc.latitude,
            longitude = loc.longitude,
            accuracy = loc.accuracy,
            altitude = loc.altitude,
            timestamp = loc.time
        )
    }

    override fun onLocationChanged(location: Location) {
        updateLocationData(location)
    }

    override fun onProviderEnabled(provider: String) {
        checkProviders()
    }

    override fun onProviderDisabled(provider: String) {
        checkProviders()
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}
