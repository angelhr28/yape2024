package com.angelhr28.yapechallenge2024.ui.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelhr28.domain.usecase.GetRecipeByIdUseCase
import com.angelhr28.domain.util.GenericException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun handleUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            handleEvent(uiEvent)
        }
    }

    private suspend fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.GetRecipe -> getRecipe(uiEvent.recipeId)
            is UiEvent.ShowMapButtonClick -> updateState { it.copy(navigateToMap = true) }
            is UiEvent.MapShown -> updateState { it.copy(navigateToMap = false) }
            is UiEvent.ErrorShown -> updateState { it.copy(error = null) }
        }
    }

    private suspend fun getRecipe(recipeId: String) {
        try {
            if (_uiState.value.recipe != null) return
            val recipe = getRecipeByIdUseCase(recipeId) ?: throw GenericException()
            updateState { it.copy(recipe = recipe) }
        } catch (throwable: Throwable) {
            updateState { it.copy(error = throwable) }
        }
    }

    private suspend fun updateState(update: (UiState) -> UiState) {
        _uiState.value = update(_uiState.value)
    }
}