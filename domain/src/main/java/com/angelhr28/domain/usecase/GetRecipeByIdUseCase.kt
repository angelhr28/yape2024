package com.angelhr28.domain.usecase

import com.angelhr28.domain.model.Recipe
import com.angelhr28.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeByIdUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipeId: String): Recipe? =
        recipeRepository.getRecipeById(recipeId)
}
