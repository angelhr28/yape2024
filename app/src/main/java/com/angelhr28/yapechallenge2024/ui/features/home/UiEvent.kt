package com.angelhr28.yapechallenge2024.ui.features.home

sealed class UiEvent {
    data object LoadRecipes : UiEvent()
    data object RefreshRecipes : UiEvent()
    data class Search(val text: String) : UiEvent()
    data class RecipeItemClick(val recipeId: String) : UiEvent()
    data object DetailShown : UiEvent()
    data object ErrorShown : UiEvent()
}
