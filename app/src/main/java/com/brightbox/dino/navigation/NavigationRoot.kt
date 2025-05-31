package com.brightbox.dino.navigation

import android.content.Intent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brightbox.dino.SettingsActivity
import com.brightbox.dino.views.applications_limit.ApplicationsLimitView
import com.brightbox.dino.views.applications_limit.SelectApplicationsToLimitView
import com.brightbox.dino.views.home.HomeView
import com.brightbox.dino.views.manage_elements.ManageTasksHabitsAndCategoriesView

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavController no provisto. Asegúrate de proveerlo usando CompositionLocalProvider.")
}

@Composable
fun NavigationRoot() {

    val navController = rememberNavController()
    val context = LocalContext.current

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(500)
                )
            }
        ) {
            composable<HomeRoute> {
                HomeView(
                    onNavigateToPreferences = {
                        val intent = Intent(context, SettingsActivity::class.java)
                        context.startActivity(intent)
                    },
                )
            }

            composable<ApplicationsLimitRoute> {
                ApplicationsLimitView(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToSelectApplicationToLimit = {
                        navController.navigate(SelectApplicationsToLimitRoute)
                    }
                )
            }

            composable<SelectApplicationsToLimitRoute> {
                SelectApplicationsToLimitView(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}