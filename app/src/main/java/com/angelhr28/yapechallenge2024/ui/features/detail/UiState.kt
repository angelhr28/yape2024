package com.angelhr28.yapechallenge2024.ui.features.detail

import com.angelhr28.domain.model.Recipe

data class UiState(
    val recipe: Recipe? = null,
    val navigateToMap: Boolean = false,
    val error: Throwable? = null
)
