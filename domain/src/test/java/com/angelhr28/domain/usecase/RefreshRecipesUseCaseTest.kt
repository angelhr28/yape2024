package com.angelhr28.domain.usecase

import com.angelhr28.domain.repository.RecipeRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RefreshRecipesUseCaseTest {

    @RelaxedMockK
    private lateinit var recipeRepository: RecipeRepository

    private val refreshRecipesUseCase by lazy { RefreshRecipesUseCase(recipeRepository) }

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `when useCase is invoked should invoke refreshFromRemoteRecipes from repository`() =
        runTest {
            refreshRecipesUseCase()

            coVerify(exactly = 1) { recipeRepository.getRecipesFromRemoteData() }
            coVerify(exactly = 0) { recipeRepository.getRecipes() }
        }
}
