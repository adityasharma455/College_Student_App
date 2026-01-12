package com.example.admin_miet_students

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen() {
    var longitude by remember { mutableStateOf<Double?>(null) }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Create MapView
    val mapView = remember {
        MapView(context).apply {
            setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }
    }

    // Create a marker for showing current location
    val marker = remember {
        Marker(mapView).apply {
            title = "You are here"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
    }

    // Update map when location changes
    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            val geoPoint = GeoPoint(latitude!!, longitude!!)
            mapView.controller.setZoom(18.5)
            mapView.controller.setCenter(geoPoint)

            // Add marker to map
            marker.position = geoPoint
            if (!mapView.overlays.contains(marker)) {
                mapView.overlays.add(marker)
            }
            mapView.invalidate()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 🗺️ Map as background
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // 🎛️ Controls over the map
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Live Location Map",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                locationPermissionState.status.isGranted -> {
                    Button(
                        onClick = {
                            GetCurrentLocation(
                                context = context,
                                onSuccess = { lat, lon ->
                                    latitude = lat
                                    longitude = lon
                                    error = null
                                },
                                onError = { err ->
                                    error = err
                                }
                            )
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Show My Location on Map")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (latitude != null && longitude != null) {
                        Text("Latitude: $latitude")
                        Text("Longitude: $longitude")
                    }

                    error?.let {
                        Text("Error: $it", color = MaterialTheme.colorScheme.error)
                    }
                }

                locationPermissionState.status.shouldShowRationale -> {
                    Text("We need location permission to show your location on map.")
                    Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                        Text("Grant Permission")
                    }
                }

                else -> {
                    Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                        Text("Request Location Permission")
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun GetCurrentLocation(
    context: Context,
    onSuccess: (Double, Double) -> Unit,
    onError: (String) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onSuccess(location.latitude, location.longitude)
        } else {
            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { freshLocation ->
                if (freshLocation != null) {
                    onSuccess(freshLocation.latitude, freshLocation.longitude)
                } else {
                    onError("Unable to fetch location. Try again.")
                }
            }.addOnFailureListener {
                onError("Error getting location: ${it.message}")
            }
        }
    }.addOnFailureListener {
        onError("Error getting location: ${it.message}")
    }
}
