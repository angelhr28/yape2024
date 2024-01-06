package com.angelhr28.yapechallenge2024.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.angelhr28.yapechallenge2024.ui.features.detail.DetailScreen
import com.angelhr28.yapechallenge2024.ui.features.home.HomeScreen
import com.angelhr28.yapechallenge2024.ui.features.map.MapScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destination.Home.route) {
        composable(Destination.Home.route) {
            HomeScreen(
                onNavigateToDetail = { recipeId ->
                    navController.navigate(
                        Destination.Detail.createNavRoute(recipeId),
                        navOptions { launchSingleTop = true }
                    )
                }
            )
        }
        composable(Destination.Detail.route) { backStackEntry ->
            val recipeId =
                requireNotNull(backStackEntry.arguments?.getString(Destination.Detail.argRecipeId))
            DetailScreen(
                recipeId = recipeId,
                onNavigateToMap = { id ->
                    navController.navigate(
                        Destination.Map.createNavRoute(id),
                        navOptions { launchSingleTop = true }
                    )
                }
            )
        }
        composable(Destination.Map.route) { backStackEntry ->
            val recipeId =
                requireNotNull(backStackEntry.arguments?.getString(Destination.Map.argRecipeId))
            MapScreen(
                recipeId = recipeId,
                onNavigateToBack = { navController.popBackStack() }
            )
        }
    }
}
