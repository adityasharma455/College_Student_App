package com.example.admin_miet_students.presentation.screens.HomeScreens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.admin_miet_students.domain.models.LocationDataModel
import com.example.admin_miet_students.presentation.navigation.Routes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentHomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.homeState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHomeScreen()
    }

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF1565C0),
            Color(0xFF42A5F5)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        when (state) {

            is HomeState.Loading -> {
                CircularProgressIndicator(color = Color.White)
            }

            is HomeState.Error -> {
                Text(
                    text = (state as HomeState.Error).message,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            is HomeState.Success -> {
                val student = (state as HomeState.Success).student
                val location = (state as HomeState.Success).location
                HomeContent(student.name, student.busId, location, navController)
            }
        }
    }
}

@Composable
fun HomeContent(
    name: String,
    busId: String,
    location: LocationDataModel?,
    navController: NavController
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            Text("Welcome, $name", fontSize = 22.sp, color = Color(0xFF1565C0))
            Text("Bus Assigned: $busId", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Live Bus Location:",
                fontSize = 18.sp,
                color = Color(0xFF1565C0)
            )

            if (location == null) {
                Text("Fetching location...", color = Color.Gray)
            } else {
                Text("Latitude: ${location.latitude}")
                Text("Longitude: ${location.longitude}")
                Text("Last Updated: ${location.lastUpdated}")
            }
            // ⭐ Add Track Bus Button
            Button(
                onClick = {navController.navigate(Routes.MapScreenRoutes)},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Track Bus on Map")
            }
        }
    }
}
