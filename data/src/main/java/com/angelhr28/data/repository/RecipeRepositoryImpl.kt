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

    /**
     * Obtenemos una receta por su id
     * @param recipeId El id de la receta.
     * @return La receta con el id especificado.
     */
    override suspend fun getRecipeById(recipeId: String): Recipe? = withContext(ioDispatcher) {
        recipeLocalDataSource.getRecipeById(recipeId)?.toDomain()
    }

    /**
     * Obtenemos las recetas.
     * @return Las recetas.
     */
    override fun getRecipes(): Flow<List<Recipe>> = flow {
        //Validamos si hay recetas en la base de datos local
        val localRecipes = getRecipesFromLocalData().firstOrNull()
        if (!localRecipes.isNullOrEmpty()) {
            //Si hay recetas en la base de datos local, las emitimos
            emit(localRecipes)
        } else {
            //Si no hay recetas en la base de datos local, las obtenemos del servidor
            val remoteRecipes = getRecipesFromRemoteData()
            emit(remoteRecipes)
        }
    }.flowOn(ioDispatcher)

    /**
     * Obtenemos las recetas del servidor.
     * @return Las recetas del servidor.
     */
    override suspend fun getRecipesFromRemoteData(): List<Recipe> {
        val recipes = recipeRemoteDataSource.getRecipes().map(RecipeResponse::toDomain)
        if (recipes.isNotEmpty()) {
            recipeLocalDataSource.saveRecipes(recipes.map(Recipe::toRoomEntity))
        }
        return recipes
    }

    /**
     * Obtenemos las recetas de la base de datos local.
     * @return Las recetas de la base de datos local.
     */
    private fun getRecipesFromLocalData(): Flow<List<Recipe>> {
        return recipeLocalDataSource.getRecipes().map { recipes ->
            recipes.map(RecipeEntity::toDomain)
        }
    }
}