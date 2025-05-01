package com.brightbox.hourglass.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brightbox.hourglass.viewmodel.ApplicationsViewModel
import com.brightbox.hourglass.viewmodel.CategoriesViewModel
import com.brightbox.hourglass.viewmodel.TasksViewModel
import com.brightbox.hourglass.views.home.HomeView
import com.brightbox.hourglass.views.settings.SettingsView

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()

    val applicationsViewModel: ApplicationsViewModel = viewModel()
    val tasksViewModel: TasksViewModel = viewModel()
    val categoriesViewModel: CategoriesViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        composable<HomeRoute> {
            HomeView(
                onNavigateToSettings = {
                    navController.navigate(
                        route = SettingsRoute,

                    )
                },
                applicationsViewModel = applicationsViewModel,
                tasksViewModel = tasksViewModel,
                categoriesViewModel = categoriesViewModel,
            )
        }

        composable<SettingsRoute> {
            SettingsView(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}