package com.example.admin_miet_students.presentation.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation{

    @Serializable
    object AuthScreenRoutes : SubNavigation()

    @Serializable
    object HomeScreenRoutes: SubNavigation()

    @Serializable
    object StudentProfileRoutes: SubNavigation()
}

sealed class Routes{

    @Serializable
    object StudentSignUpScreenRoutes

    @Serializable
    object StudentLogInScreenRoutes

    @Serializable
    object HomeScreenRoutes

    @Serializable
    object StudentProfileScreenRoutes

    @Serializable
    object MapScreenRoutes

}
