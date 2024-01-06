package com.angelhr28.data.datasource.local.recipe

import com.angelhr28.data.room.dao.RecipeDao
import com.angelhr28.data.room.entity.RecipeEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecipeLocalDataSourceImplTest {

    @RelaxedMockK
    private lateinit var recipeDao: RecipeDao

    private val recipeLocalDataSource by lazy { RecipeLocalDataSourceImpl(recipeDao) }

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `when saveRecipes is invoked should invoke save from dao`() = runTest {
        val data = listOf(mockk<RecipeEntity>())

        recipeLocalDataSource.saveRecipes(data)

        coVerify(exactly = 1) { recipeDao.save(data) }
        coVerify(exactly = 0) { recipeDao.getRecipes() }
        coVerify(exactly = 0) { recipeDao.getRecipeById(any()) }
    }

    @Test
    fun `when getRecipes is invoked should return flow from dao`() {
        val testFlow: Flow<List<RecipeEntity>> = flow {
            emit(listOf(mockk()))
        }

        coEvery { recipeDao.getRecipes() } returns testFlow
        val flowResult = recipeLocalDataSource.getRecipes()

        assertEquals(flowResult, testFlow)
        coVerify(exactly = 0) { recipeDao.save(any()) }
        coVerify(exactly = 1) { recipeDao.getRecipes() }
        coVerify(exactly = 0) { recipeDao.getRecipeById(any()) }
    }

    @Test
    fun `when getRecipeById is invoked should return RecipeEntity from dao`() = runTest {
        val recipeId = "123"
        val recipeEntity: RecipeEntity = mockk()

        coEvery { recipeDao.getRecipeById(any()) } returns recipeEntity
        val result = recipeLocalDataSource.getRecipeById(recipeId)

        assertEquals(result, recipeEntity)
        coVerify(exactly = 0) { recipeDao.save(any()) }
        coVerify(exactly = 0) { recipeDao.getRecipes() }
        coVerify(exactly = 1) { recipeDao.getRecipeById(any()) }
    }
}
