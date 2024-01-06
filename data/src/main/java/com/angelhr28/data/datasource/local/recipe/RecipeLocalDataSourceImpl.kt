package com.angelhr28.data.datasource.local.recipe

import com.angelhr28.data.room.dao.RecipeDao
import com.angelhr28.data.room.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeLocalDataSourceImpl @Inject constructor(
    private val dao: RecipeDao
) : RecipeLocalDataSource {

    override suspend fun saveRecipes(data: List<RecipeEntity>) = dao.save(data)

    override fun getRecipes(): Flow<List<RecipeEntity>> = dao.getRecipes()

    override suspend fun getRecipeById(recipeId: String): RecipeEntity? =
        dao.getRecipeById(recipeId)
}
