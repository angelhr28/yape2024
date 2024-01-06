package com.angelhr28.yapechallenge2024.ui.features.map

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.angelhr28.domain.model.Recipe
import com.angelhr28.yapechallenge2024.R
import com.angelhr28.yapechallenge2024.ui.components.BaseScreen
import com.angelhr28.yapechallenge2024.ui.components.ErrorComponent
import com.angelhr28.yapechallenge2024.ui.components.LoadingComponent
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    recipeId: String,
    viewModel: MapViewModel = hiltViewModel(),
    onNavigateToBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Cargar el Recipe si es nulo
    LaunchedEffect(recipeId) {
        if (uiState.recipe == null) {
            viewModel.handleUiEvent(UiEvent.GetRecipe(recipeId))
        }
    }

    // Mostrar componentes según el estado
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingComponent(modifier = Modifier.fillMaxSize())
            uiState.recipe != null -> {
                ScreenContent(
                    recipe = uiState.recipe,
                    onBackClick = onNavigateToBack
                )
            }
        }

        // Mostrar error si está presente
        uiState.error?.let { error ->
            ErrorComponent(
                error = error,
                onDismiss = { viewModel.handleUiEvent(UiEvent.ErrorShown) }
            )
        }
    }
}

@Composable
private fun ScreenContent(recipe: Recipe?, onBackClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        val cameraPositionState = rememberCameraPositionState()
        recipe?.let {
            val markerPosition = LatLng(it.originLocation.latitude, it.originLocation.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(markerPosition, 12f)

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = { println("XD") }
            ) {
                Marker(
                    state = MarkerState(position = markerPosition),
                    title = recipe.name,
                    snippet = stringResource(id = R.string.it_originated_here)
                )
            }
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(id = R.string.icon_back),
                tint = Color.Black,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(onClick = onBackClick)
                    .padding(8.dp)
            )
        }
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    BaseScreen {
        ScreenContent(recipe = Recipe(id = "1", name = "Ceviche")) {}
    }
}
