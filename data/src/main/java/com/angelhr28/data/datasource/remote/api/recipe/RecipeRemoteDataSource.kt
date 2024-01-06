package com.angelhr28.data.datasource.remote.api.recipe

import com.angelhr28.data.datasource.remote.response.RecipeResponse

interface RecipeRemoteDataSource {
    suspend fun getRecipes(): List<RecipeResponse>
}
