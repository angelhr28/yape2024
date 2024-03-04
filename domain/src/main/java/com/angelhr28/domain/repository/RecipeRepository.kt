package com.angelhr28.domain.repository

import com.angelhr28.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getRecipes(): Flow<List<Recipe>>

    suspend fun getRecipeById(recipeId: String): Recipe?

    suspend fun getRecipesFromRemoteData(): List<Recipe>
}
