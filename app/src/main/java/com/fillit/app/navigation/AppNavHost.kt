package com.fillit.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fillit.app.ui.screens.AddScheduleScreen
import com.fillit.app.ui.screens.OnboardingScreen
import com.fillit.app.ui.screens.PlaceDetailScreen
import com.fillit.app.ui.screens.RecommendationScreen
import com.fillit.app.ui.screens.ScheduleViewScreen
import com.fillit.app.ui.screens.SettingsScreen
import com.fillit.app.ui.screens.SavedPlacesScreen



@Composable
    fun AppNavHost(
        navController: NavHostController,
        start: Route = Route.Onboarding
    ) {
        NavHost(navController = navController, startDestination = start.name) {
            composable(Route.Onboarding.name) {
                OnboardingScreen(
                    onGoogleLogin = { /* TODO: Implement Google login navigation or logic */ },
                    onStart = { navController.navigate(Route.Schedule.name) },
                    onAllowLocation = { /* TODO: Implement allow location navigation or logic */ }
                )
            }
            composable(Route.Schedule.name) {
                ScheduleViewScreen(
                    current = Route.Schedule,
                    onNavigate = { route -> navController.navigate(route.name) },
                    onAddClick = { navController.navigate(Route.AddSchedule.name) }
                )
            }
            composable(Route.AddSchedule.name) {
                AddScheduleScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }
            composable(Route.Recommendations.name) {
                RecommendationScreen(
                    current = Route.Recommendations,
                    onBack = { navController.popBackStack() },
                    onFilter = { /* TODO: Implement filter action */ },
                    onNavigate = { route -> navController.navigate(route.name) }
                )
            }
            composable(Route.PlaceDetail.name) {
                PlaceDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Route.SavedPlaces.name) {
                SavedPlacesScreen(
                    current = Route.SavedPlaces,
                    onBack = { navController.popBackStack() },
                    onFilter = { /* TODO: Implement filter action */ },
                    onNavigate = { route: Route -> navController.navigate(route.name) }
                )
            }
            composable(Route.Settings.name) {
                SettingsScreen(
                    current = Route.Settings,
                    onBack = { navController.popBackStack() },
                    onNavigate = { route -> navController.navigate(route.name) }
                )
            }
        }
    }