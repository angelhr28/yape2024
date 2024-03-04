package com.angelhr28.domain.usecase

import com.angelhr28.domain.model.Recipe
import com.angelhr28.domain.repository.RecipeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRecipeByIdUseCaseTest {

    @RelaxedMockK
    private lateinit var recipeRepository: RecipeRepository

    private val getRecipeByIdUseCase by lazy { GetRecipeByIdUseCase(recipeRepository) }

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `when useCase is invoked should return recipe from repository`() = runTest {
        val recipeId = "123"
        val recipe: Recipe = mockk()

        coEvery { recipeRepository.getRecipeById(any()) } returns recipe
        val result = getRecipeByIdUseCase(recipeId)

        assertEquals(result, recipe)
        coVerify(exactly = 1) { recipeRepository.getRecipeById(recipeId) }
        coVerify(exactly = 0) { recipeRepository.getRecipes() }
        coVerify(exactly = 0) { recipeRepository.getRecipesFromRemoteData() }
    }
}
