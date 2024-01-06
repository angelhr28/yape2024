package com.angelhr28.yapechallenge2024.features.home

import com.angelhr28.domain.model.Recipe
import com.angelhr28.domain.usecase.GetRecipesUseCase
import com.angelhr28.domain.usecase.RefreshRecipesUseCase
import com.angelhr28.domain.util.GenericException
import com.angelhr28.yapechallenge2024.ui.features.home.HomeListViewModel
import com.angelhr28.yapechallenge2024.ui.features.home.UiEvent
import com.angelhr28.yapechallenge2024.util.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getRecipesUseCase: GetRecipesUseCase

    @RelaxedMockK
    private lateinit var refreshRecipesUseCase: RefreshRecipesUseCase

    private val viewModel by lazy {
        HomeListViewModel(getRecipesUseCase, refreshRecipesUseCase)
    }

    private val recipeId1 = "123"
    private val recipeId2 = "456"
    private val recipeList = listOf(
        Recipe(id = recipeId1, name = "Ceviche", ingredients = listOf("Pesacado", "Sal")),
        Recipe(id = recipeId2, ingredients = listOf("Tomate", "Sal al gusto"))
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { getRecipesUseCase() } returns flow {
            emit(recipeList)
        }
    }

    @Test
    fun `load recipes should display the fetched list`() = runTest {
        viewModel.handleUiEvent(UiEvent.LoadRecipes)
        assertEquals(viewModel.uiState.value.recipes, recipeList)
    }

    @Test
    fun `search recipes should filter the list based on search text`() = runTest {
        viewModel.handleUiEvent(UiEvent.Search("Cevi"))
        assertEquals(viewModel.uiState.value.recipes.first().id, recipeId1)

        viewModel.handleUiEvent(UiEvent.Search("Tomat"))
        assertEquals(viewModel.uiState.value.recipes.first().id, recipeId2)

        viewModel.handleUiEvent(UiEvent.Search("Sal"))
        assertEquals(viewModel.uiState.value.recipes.size, 2)
    }

    @Test
    fun `refresh recipes should call refresh use case`() = runTest {
        viewModel.handleUiEvent(UiEvent.RefreshRecipes)
        coVerify(exactly = 1) { refreshRecipesUseCase() }
    }

    @Test
    fun `clicking a recipe item should set selected recipe ID`() = runTest {
        viewModel.handleUiEvent(UiEvent.RecipeItemClick(recipeId1))
        assertEquals(viewModel.uiState.value.selectedRecipeId, recipeId1)
    }

    @Test
    fun `when detail is shown, selected recipe ID should be null`() = runTest {
        viewModel.handleUiEvent(UiEvent.DetailShown)
        assertEquals(viewModel.uiState.value.selectedRecipeId, null)
    }

    @Test
    fun `refresh recipes should handle error and set loading state to false`() = runTest {
        coEvery { refreshRecipesUseCase() } throws GenericException()

        viewModel.handleUiEvent(UiEvent.RefreshRecipes)

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.error != null)
    }

    @Test
    fun `when error is shown, error state should be reset`() = runTest {
        viewModel.handleUiEvent(UiEvent.ErrorShown)
        assertEquals(viewModel.uiState.value.error, null)
    }

    @Test
    fun `given null recipe flow, when loading recipes, should not crash`() = runTest {
        coEvery { getRecipesUseCase() } returns flow { }

        viewModel.handleUiEvent(UiEvent.LoadRecipes)

        assertEquals(viewModel.uiState.value.recipes.size, 0)
    }

    @Test
    fun `given error while refreshing recipes, should set error state`() = runTest {
        coEvery { refreshRecipesUseCase() } throws GenericException()

        viewModel.handleUiEvent(UiEvent.RefreshRecipes)

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.error != null)
    }

}
