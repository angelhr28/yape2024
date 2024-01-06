package com.angelhr28.domain.usecase

import com.angelhr28.domain.model.Recipe
import com.angelhr28.domain.repository.RecipeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRecipesUseCaseTest {

    @RelaxedMockK
    private lateinit var recipeRepository: RecipeRepository

    private val getRecipesUseCase by lazy { GetRecipesUseCase(recipeRepository) }

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `when useCase is invoked should return flow from repository`() {
        val testFlow: Flow<List<Recipe>> = flow {
            emit(listOf(mockk()))
        }

        coEvery { recipeRepository.getRecipes() } returns testFlow
        val flowResult = getRecipesUseCase()

        assertEquals(testFlow, flowResult)
        coVerify(exactly = 1) { recipeRepository.getRecipes() }
        coVerify(exactly = 0) { recipeRepository.refreshFromRemoteRecipes() }
    }
}
