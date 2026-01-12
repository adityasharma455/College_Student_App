package com.example.admin_miet_students.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.admin_miet_students.MapScreen
import com.example.admin_miet_students.presentation.screens.AuthScreens.LogInScreens.StudentLogInScreen
import com.example.admin_miet_students.presentation.screens.AuthScreens.RegisterScreens.StudentSignUpScreen
import com.example.admin_miet_students.presentation.screens.HomeScreens.StudentHomeScreen
import com.example.admin_miet_students.presentation.screens.ProfileScreens.StudentProfileScreen
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define authentication routes
    val authRoutes = setOf(
        Routes.StudentLogInScreenRoutes,
        Routes.StudentSignUpScreenRoutes
    )

    // Show bottom bar only if logged in & not on login/signup screens
    val showBottomBar = currentRoute != null &&
            !authRoutes.contains(currentRoute) &&
            firebaseAuth.currentUser != null

    // Decide start screen based on login status
    val startScreen = if (firebaseAuth.currentUser == null) {
        SubNavigation.AuthScreenRoutes
    } else {
        SubNavigation.HomeScreenRoutes
    }

    // Define bottom navigation items
    val bottomNavItems = listOf(
        BottomItem(title = "Home", icon = Icons.Default.Home),
        BottomItem(title = "Profile", icon = Icons.Default.AccountCircle)
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                AnimatedBottomBar(
                    modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
                    selectedItem = selectedItem,
                    itemSize = bottomNavItems.size,
                    containerColor = Color.Transparent,
                    indicatorStyle = IndicatorStyle.FILLED,
                    indicatorColor = Color(0xFF1565C0),
                    indicatorDirection = IndicatorDirection.BOTTOM
                ) {
                    bottomNavItems.forEachIndexed { index, item ->
                        BottomBarItem(
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                when (index) {
                                    0 -> navController.navigate(Routes.HomeScreenRoutes) {
                                        popUpTo(SubNavigation.HomeScreenRoutes) { inclusive = true }
                                    }
                                    1 -> navController.navigate(Routes.StudentProfileScreenRoutes){
                                        popUpTo(SubNavigation.HomeScreenRoutes) { inclusive = true }
                                    }
                                }
                            },
                            imageVector = item.icon,
                            label = item.title,
                            containerColor = Color.Transparent
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = startScreen) {

                // AUTH ROUTES
                navigation<SubNavigation.AuthScreenRoutes>(
                    startDestination = Routes.StudentLogInScreenRoutes
                ) {
                    composable<Routes.StudentLogInScreenRoutes> {
                       StudentLogInScreen(navController = navController)
                    }
                    composable<Routes.StudentSignUpScreenRoutes> {
                        StudentSignUpScreen(navController= navController)
                    }
                }

                // HOME ROUTES
                navigation<SubNavigation.HomeScreenRoutes>(
                    startDestination = Routes.HomeScreenRoutes
                ) {
                    composable<Routes.HomeScreenRoutes> {
                        StudentHomeScreen(navController = navController)
                    }
                    composable<Routes.MapScreenRoutes>{
                        MapScreen(navController = navController)
                    }
                }


                // PROFILE ROUTES
                navigation<SubNavigation.StudentProfileRoutes>(
                    startDestination = Routes.StudentProfileScreenRoutes
                ) {
                    composable<Routes.StudentProfileScreenRoutes> {
                        StudentProfileScreen(navController = navController)
                    }
                }
            }
        }
    }
}

data class BottomItem(
    val title: String,
    val icon: ImageVector
)
