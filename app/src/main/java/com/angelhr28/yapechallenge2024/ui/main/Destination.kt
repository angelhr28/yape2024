package com.angelhr28.yapechallenge2024.ui.main

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(val route: String) {
    data object Home : Destination("home")
    data object Detail : Destination("detail/{recipeId}") {
        const val argRecipeId = "recipeId"
        fun createNavRoute(recipeId: String): String = "detail/$recipeId"
        fun createNavArg() = navArgument(argRecipeId) { type = NavType.StringType }
    }

    data object Map : Destination("map/{recipeId}") {
        const val argRecipeId = "recipeId"
        fun createNavRoute(recipeId: String): String = "map/$recipeId"
        fun createNavArg() = navArgument(argRecipeId) { type = NavType.StringType }
    }
}

