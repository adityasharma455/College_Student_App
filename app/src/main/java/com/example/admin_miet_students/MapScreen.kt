package com.example.admin_miet_students

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.admin_miet_students.presentation.screens.HomeScreens.HomeState
import com.example.admin_miet_students.presentation.screens.HomeScreens.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val state by homeViewModel.homeState.collectAsState()

    // 👇 Start listening to Firebase here also
    LaunchedEffect(Unit) {
        homeViewModel.loadHomeScreen()
    }

    AndroidView(
        factory = { context ->

            val map = MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(16.0)
            }

            // Create marker once
            val marker = Marker(map).apply {
                title = "Bus Location"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }

            map.overlays.add(marker)  // add once

            map
        },
        update = { map ->

            if (state is HomeState.Success) {
                val location = (state as HomeState.Success).location

                if (location != null) {
                    println("🔥 BUS LOCATION -> lat=${location.latitude}, lon=${location.longitude}")
                    Log.d("Lat=${location.latitude}", "lon=${location.longitude}")

                    val geo = GeoPoint(location.latitude, location.longitude)

                    // Update marker position
                    val marker = map.overlays.filterIsInstance<Marker>().first()
                    marker.position = geo

                    // Move map
                    map.controller.animateTo(geo)

                    map.invalidate()
                }
            }
        }
    )
}

