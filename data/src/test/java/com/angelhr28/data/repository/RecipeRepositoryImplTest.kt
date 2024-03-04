package com.angelhr28.data.repository

import com.angelhr28.data.datasource.local.recipe.RecipeLocalDataSource
import com.angelhr28.data.datasource.remote.api.recipe.RecipeRemoteDataSource
import com.angelhr28.data.datasource.remote.response.RecipeResponse
import com.angelhr28.data.room.entity.RecipeEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecipeRepositoryImplTest {

    @RelaxedMockK
    private lateinit var localDataSource: RecipeLocalDataSource

    @RelaxedMockK
    private lateinit var remoteDataSource: RecipeRemoteDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val repository by lazy {
        RecipeRepositoryImpl(testDispatcher, localDataSource, remoteDataSource)
    }

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `when getRecipes is invoked should invoke getRecipes from dataSource`() {
        repository.getRecipes()

        coVerify(exactly = 1) { localDataSource.getRecipes() }
        coVerify(exactly = 0) { localDataSource.getRecipeById(any()) }
        coVerify(exactly = 0) { remoteDataSource.getRecipes() }
        coVerify(exactly = 0) { localDataSource.saveRecipes(any()) }
    }

    @Test
    fun `when getRecipeById is invoked should invoke getRecipeById from dataSource`() = runTest {
        val recipeId = "123"
        val recipeEntity: RecipeEntity = mockk(relaxed = true)

        coEvery { recipeEntity.id } returns recipeId
        coEvery { recipeEntity.ingredients } returns "[]"
        coEvery { recipeEntity.originLocation } returns "{}"
        coEvery { localDataSource.getRecipeById(any()) } returns recipeEntity
        val result = repository.getRecipeById(recipeId)

        assertEquals(result?.id, recipeEntity.id)
        coVerify(exactly = 0) { localDataSource.getRecipes() }
        coVerify(exactly = 1) { localDataSource.getRecipeById(recipeId) }
        coVerify(exactly = 0) { remoteDataSource.getRecipes() }
        coVerify(exactly = 0) { localDataSource.saveRecipes(any()) }
    }

    @Test
    fun `when dataSource return elements should invoke saveRecipes`() = runTest {
        val recipes: List<RecipeResponse> = listOf(mockk(relaxed = true))

        coEvery { remoteDataSource.getRecipes() } returns recipes
        repository.getRecipesFromRemoteData()

        coVerify(exactly = 0) { localDataSource.getRecipes() }
        coVerify(exactly = 0) { localDataSource.getRecipeById(any()) }
        coVerify(exactly = 1) { remoteDataSource.getRecipes() }
        coVerify(exactly = 1) { localDataSource.saveRecipes(any()) }
        coVerifyOrder {
            remoteDataSource.getRecipes()
            localDataSource.saveRecipes(any())
        }
    }

    @Test
    fun `when dataSource return empty list should not invoke saveRecipes`() = runTest {
        coEvery { remoteDataSource.getRecipes() } returns emptyList()
        repository.getRecipesFromRemoteData()

        coVerify(exactly = 0) { localDataSource.getRecipes() }
        coVerify(exactly = 0) { localDataSource.getRecipeById(any()) }
        coVerify(exactly = 1) { remoteDataSource.getRecipes() }
        coVerify(exactly = 0) { localDataSource.saveRecipes(any()) }
    }
}
