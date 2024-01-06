package com.angelhr28.yapechallenge2024.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelhr28.domain.model.Recipe
import com.angelhr28.domain.usecase.GetRecipesUseCase
import com.angelhr28.domain.usecase.RefreshRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val refreshRecipesUseCase: RefreshRecipesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var recipeList: List<Recipe> = emptyList()
    private val textToSearch: String
        get() = _uiState.value.textToSearch

    init {
        handleUiEvent(UiEvent.LoadRecipes)
    }

    fun handleUiEvent(uiEvent: UiEvent) = viewModelScope.launch {
        when (uiEvent) {
            is UiEvent.LoadRecipes -> loadRecipes()
            is UiEvent.RefreshRecipes -> refreshRecipes()
            is UiEvent.Search -> {
                _uiState.value.textToSearch = uiEvent.text.trim()
                searchRecipes()
            }

            is UiEvent.RecipeItemClick -> {
                _uiState.update { it.copy(selectedRecipeId = uiEvent.recipeId) }
            }

            is UiEvent.DetailShown -> {
                _uiState.update { it.copy(selectedRecipeId = null) }
            }

            is UiEvent.ErrorShown -> _uiState.update { it.copy(error = null) }
        }
    }

    private suspend fun loadRecipes() {
        try {
            getRecipesUseCase().collect { recipes ->
                recipeList = recipes
                searchRecipes()
                if (recipes.isEmpty()) {
                    refreshRecipes()
                }
            }
        } catch (throwable: Throwable) {
            _uiState.update { it.copy(isLoading = false, error = throwable) }
        }
    }

    private suspend fun refreshRecipes() {
        try {
            _uiState.update { it.copy(isLoading = true) }
            refreshRecipesUseCase()
            _uiState.update { it.copy(isLoading = false) }
        } catch (throwable: Throwable) {
            _uiState.update { it.copy(isLoading = false, error = throwable) }
        }
    }

    private fun searchRecipes() {
        val listResult = when {
            textToSearch.isBlank() -> recipeList
            else -> recipeList.filter(::predicateToSearch)
        }
        _uiState.update { it.copy(isLoading = false, recipes = listResult) }
    }

    private fun predicateToSearch(recipe: Recipe): Boolean {
        if (recipe.name.contains(textToSearch, true)) {
            return true
        }
        return recipe.ingredients.any { it.contains(textToSearch, true) }
    }
}
