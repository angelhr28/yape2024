package com.angelhr28.data.repository

import com.angelhr28.data.datasource.local.recipe.RecipeLocalDataSource
import com.angelhr28.data.datasource.remote.api.recipe.RecipeRemoteDataSource
import com.angelhr28.data.datasource.remote.response.RecipeResponse
import com.angelhr28.data.mapper.toDomain
import com.angelhr28.data.mapper.toRoomEntity
import com.angelhr28.data.room.entity.RecipeEntity
import com.angelhr28.domain.model.Recipe
import com.angelhr28.domain.repository.RecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val recipeLocalDataSource: RecipeLocalDataSource,
    private val recipeRemoteDataSource: RecipeRemoteDataSource
) : RecipeRepository {

    override suspend fun getRecipeById(recipeId: String): Recipe? = withContext(ioDispatcher) {
        recipeLocalDataSource.getRecipeById(recipeId)?.toDomain()
    }

    override fun getRecipes(): Flow<List<Recipe>> = flow {
        val localRecipes = getRecipesFromLocalData().firstOrNull()
        if (!localRecipes.isNullOrEmpty()) {
            emit(localRecipes)
        } else {
            val remoteRecipes = getRecipesFromRemoteData()
            emit(remoteRecipes)
        }
    }.flowOn(ioDispatcher)

    override suspend fun getRecipesFromRemoteData(): List<Recipe> {
        val recipes = recipeRemoteDataSource.getRecipes().map(RecipeResponse::toDomain)
        if (recipes.isNotEmpty()) {
            recipeLocalDataSource.saveRecipes(recipes.map(Recipe::toRoomEntity))
        }
        return recipes
    }

    private fun getRecipesFromLocalData(): Flow<List<Recipe>> {
        return recipeLocalDataSource.getRecipes().map { recipes ->
            recipes.map(RecipeEntity::toDomain)
        }
    }
}