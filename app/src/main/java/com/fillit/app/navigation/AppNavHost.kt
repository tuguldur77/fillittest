// kotlin
package com.fillit.app.navigation

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fillit.app.R
import com.fillit.app.model.SessionManager
import com.fillit.app.ui.screens.AddScheduleScreen
import com.fillit.app.ui.screens.OnboardingScreen
import com.fillit.app.ui.screens.PlaceDetailScreen
import com.fillit.app.ui.screens.RecommendationScreen
import com.fillit.app.ui.screens.ScheduleViewModel
import com.fillit.app.ui.screens.ScheduleViewScreen
import com.fillit.app.ui.screens.SavedPlacesScreen
import com.fillit.app.ui.screens.SettingsScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    start: Route = Route.Onboarding
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnSuccessListener { authResult ->
                    authResult.user?.uid?.let { userId ->
                        SessionManager.saveUserId(userId)
                    }
                    navController.navigate(Route.Schedule.name) {
                        popUpTo(Route.Onboarding.name) { inclusive = true }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        e.localizedMessage ?: "Sign‑in failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: ApiException) {
            Toast.makeText(
                context,
                "Google sign‑in failed: ${e.statusCode}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    NavHost(navController = navController, startDestination = start.name) {
        composable(Route.Onboarding.name) {
            OnboardingScreen(
                onGoogleLogin = { signInLauncher.launch(googleSignInClient.signInIntent) },
                onStart = { navController.navigate(Route.Schedule.name) },
                onAllowLocation = { /* TODO: request location permission */ }
            )
        }
        composable(Route.Schedule.name) {
            val scheduleViewModel: ScheduleViewModel = viewModel()
            ScheduleViewScreen(
                current = Route.Schedule,
                onNavigate = { route -> navController.navigate(route.name) },
                onAddClick = { navController.navigate(Route.AddSchedule.name) },
                scheduleViewModel = scheduleViewModel
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
                onFilter = { /* TODO */ },
                onNavigate = { route -> navController.navigate(route.name) }
            )
        }
        composable(Route.PlaceDetail.name) {
            PlaceDetailScreen(onBack = { navController.popBackStack() })
        }
        composable(Route.SavedPlaces.name) {
            SavedPlacesScreen(
                current = Route.SavedPlaces,
                onBack = { navController.popBackStack() },
                onFilter = { /* TODO */ },
                onNavigate = { route -> navController.navigate(route.name) }
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
