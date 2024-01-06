package com.angelhr28.yapechallenge2024.ui.features.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelhr28.domain.usecase.GetRecipeByIdUseCase
import com.angelhr28.domain.util.GenericException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun handleUiEvent(uiEvent: UiEvent) = viewModelScope.launch {
        when (uiEvent) {
            is UiEvent.GetRecipe -> getRecipe(uiEvent.recipeId)
            is UiEvent.ErrorShown -> clearError()
        }
    }

    private suspend fun getRecipe(recipeId: String) {
        try {
            if (_uiState.value.recipe != null) return
            delay(DELAY_FOR_LOADING)
            val recipe = getRecipeByIdUseCase(recipeId) ?: throw GenericException()
            _uiState.update { it.copy(isLoading = false, recipe = recipe) }
        } catch (throwable: Throwable) {
            _uiState.update { it.copy(isLoading = false, error = throwable) }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    companion object {
        private const val DELAY_FOR_LOADING = 2000L
    }
}
