package com.angelhr28.domain.usecase

import com.angelhr28.domain.model.Recipe
import com.angelhr28.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Recipe>> = recipeRepository.getRecipes()
}
