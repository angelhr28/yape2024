package com.angelhr28.yapechallenge2024.ui.features.map

sealed interface UiEvent {
    data class GetRecipe(val recipeId: String) : UiEvent
    data object ErrorShown : UiEvent
}
