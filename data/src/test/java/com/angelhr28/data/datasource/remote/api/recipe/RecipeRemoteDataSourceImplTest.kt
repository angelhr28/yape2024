package com.angelhr28.data.datasource.remote.api.recipe

import com.angelhr28.data.datasource.remote.api.AppRestApi
import com.angelhr28.data.datasource.remote.response.RecipeResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RecipeRemoteDataSourceImplTest {

    @RelaxedMockK
    private lateinit var appRestApi: AppRestApi

    private val recipeRemoteDataSource by lazy { RecipeRemoteDataSourceImpl(appRestApi) }

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `when getRecipes is invoked should return recipe list from AppRestApi`() = runTest {
        val list = listOf(mockk<RecipeResponse>())

        coEvery { appRestApi.getRecipes() } returns Response.success(list)
        val result = recipeRemoteDataSource.getRecipes()

        assertEquals(result, list)
        coVerify(exactly = 1) { appRestApi.getRecipes() }
    }
}
