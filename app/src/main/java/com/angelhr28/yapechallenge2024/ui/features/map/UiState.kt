package com.angelhr28.yapechallenge2024.ui.features.map

import com.angelhr28.domain.model.Recipe

data class UiState(
    val isLoading: Boolean = true,
    val recipe: Recipe? = null,
    val error: Throwable? = null
)
