package com.angelhr28.yapechallenge2024.features.map

import com.angelhr28.domain.model.Recipe
import com.angelhr28.domain.usecase.GetRecipeByIdUseCase
import com.angelhr28.domain.util.GenericException
import com.angelhr28.yapechallenge2024.ui.features.map.MapViewModel
import com.angelhr28.yapechallenge2024.ui.features.map.UiEvent
import com.angelhr28.yapechallenge2024.util.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapViewModelTest{

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getRecipeByIdUseCase: GetRecipeByIdUseCase

    private val viewModel by lazy { MapViewModel(getRecipeByIdUseCase) }
    private val recipeId = "123"
    private val recipe = Recipe(id = recipeId)
    private val delayForLoading = 2500L

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `get recipe`() = runTest {
        coEvery { getRecipeByIdUseCase(any()) } returns recipe
        viewModel.handleUiEvent(UiEvent.GetRecipe(recipeId))
        delay(delayForLoading)
        assertEquals(viewModel.uiState.value.recipe, recipe)
    }

    @Test
    fun `when throw exception`() = runTest {
        coEvery { getRecipeByIdUseCase(any()) } throws GenericException()
        viewModel.handleUiEvent(UiEvent.GetRecipe(recipeId))
        delay(delayForLoading)
        assertTrue(viewModel.uiState.value.error != null)
    }

    @Test
    fun `when error is shown`() = runTest {
        viewModel.handleUiEvent(UiEvent.ErrorShown)
        assertEquals(viewModel.uiState.value.error, null)
    }
}
