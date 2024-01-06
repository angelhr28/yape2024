package com.angelhr28.yapechallenge2024.features.detail

import com.angelhr28.domain.model.Recipe
import com.angelhr28.domain.usecase.GetRecipeByIdUseCase
import com.angelhr28.domain.util.GenericException
import com.angelhr28.yapechallenge2024.ui.features.detail.DetailViewModel
import com.angelhr28.yapechallenge2024.ui.features.detail.UiEvent
import com.angelhr28.yapechallenge2024.util.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getRecipeByIdUseCase: GetRecipeByIdUseCase

    private val viewModel by lazy { DetailViewModel(getRecipeByIdUseCase) }
    private val recipeId = "123"
    private val recipe = Recipe(id = recipeId)

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `given recipe ID, when fetching recipe, should set recipe in UI state`() = runTest {
        coEvery { getRecipeByIdUseCase(any()) } returns recipe
        viewModel.handleUiEvent(UiEvent.GetRecipe(recipeId))
        assertEquals(viewModel.uiState.value.recipe, recipe)
    }

    @Test
    fun `given show map button is clicked, should set navigateToMap in UI state`() = runTest {
        viewModel.handleUiEvent(UiEvent.ShowMapButtonClick)
        assertTrue(viewModel.uiState.value.navigateToMap)
    }

    @Test
    fun `given map is shown, should reset navigateToMap in UI state`() = runTest {
        viewModel.handleUiEvent(UiEvent.MapShown)
        assertFalse(viewModel.uiState.value.navigateToMap)
    }

    @Test
    fun `given throwing an exception when fetching recipe, should set error in UI state`() =
        runTest {
            coEvery { getRecipeByIdUseCase(any()) } throws GenericException()
            viewModel.handleUiEvent(UiEvent.GetRecipe(recipeId))
            assertTrue(viewModel.uiState.value.error != null)
        }

    @Test
    fun `given error is shown, should reset error in UI state`() = runTest {
        viewModel.handleUiEvent(UiEvent.ErrorShown)
        assertEquals(viewModel.uiState.value.error, null)
    }

    @Test
    fun `given recipe ID, when fetching recipe and already exists, should not fetch again`() =
        runTest {
            viewModel.handleUiEvent(UiEvent.GetRecipe(recipeId))
            viewModel.handleUiEvent(UiEvent.GetRecipe(recipeId))
            coVerify(exactly = 1) { getRecipeByIdUseCase(any()) }
        }

}
