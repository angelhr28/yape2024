package com.angelhr28.domain.usecase

import com.angelhr28.domain.repository.RecipeRepository
import javax.inject.Inject

class RefreshRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke() = recipeRepository.getRecipesFromRemoteData()
}
