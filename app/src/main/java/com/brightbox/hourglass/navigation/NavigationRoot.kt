package com.brightbox.hourglass.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brightbox.hourglass.views.home.HomeView
import com.brightbox.hourglass.views.preferences.GeneralPreferencesView
import com.brightbox.hourglass.views.preferences.PreferencesView

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        composable<HomeRoute> {
            HomeView(
                onNavigateToPreferences = {
                    navController.navigate(route = PreferencesRoute)
                },
            )
        }

        composable<PreferencesRoute> {
            PreferencesView(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToGeneralPreferences = {
                    navController.navigate(route = GeneralPreferencesRoute)
                }
            )
        }

        composable<GeneralPreferencesRoute> {
            GeneralPreferencesView(
                onNavigateUp = {
                    navController.navigateUp()
                },
            )
        }
    }
}