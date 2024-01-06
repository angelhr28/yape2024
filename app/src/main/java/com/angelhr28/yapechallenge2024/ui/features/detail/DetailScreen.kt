package com.angelhr28.yapechallenge2024.ui.features.detail

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.angelhr28.domain.model.Recipe
import com.angelhr28.yapechallenge2024.R
import com.angelhr28.yapechallenge2024.ui.components.BaseScreen
import com.angelhr28.yapechallenge2024.ui.components.DottedShape
import com.angelhr28.yapechallenge2024.ui.components.ErrorComponent

@Composable
fun DetailScreen(
    recipeId: String,
    viewModel: DetailViewModel = hiltViewModel(),
    onNavigateToMap: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = recipeId) {
        if (uiState.recipe == null) {
            viewModel.handleUiEvent(UiEvent.GetRecipe(recipeId))
        }
    }

    if (uiState.navigateToMap) {
        uiState.recipe?.let {
            onNavigateToMap(it.id)
            viewModel.handleUiEvent(UiEvent.MapShown)
        }
    }

    uiState.recipe?.let { recipe ->
        ScreenContent(
            recipe = recipe,
            sendUiEvent = viewModel::handleUiEvent
        )
    }

    uiState.error?.let { error ->
        ErrorComponent(
            error = error,
            onDismiss = { viewModel.handleUiEvent(UiEvent.ErrorShown) }
        )
    }
}

@Composable
private fun ScreenContent(recipe: Recipe, sendUiEvent: (UiEvent) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            ImageSection(recipe = recipe)
            Spacer(modifier = Modifier.height(16.dp))
            NameSection(recipe = recipe)
            Spacer(modifier = Modifier.height(16.dp))
            IngredientSection(ingredients = recipe.ingredients)
            Spacer(modifier = Modifier.height(16.dp))
            PreparationSection(recipe = recipe)
            Spacer(modifier = Modifier.height(88.dp))
        }
        ExtendedFloatingActionButton(
            onClick = { sendUiEvent(UiEvent.ShowMapButtonClick) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = stringResource(id = R.string.icon_show_origin)
                )
            },
            text = { Text(text = stringResource(id = R.string.show_origin)) },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun ImageSection(recipe: Recipe, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = rememberAsyncImagePainter(model = recipe.imageUrl),
            contentDescription = recipe.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(4f / 3f),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .align(Alignment.BottomCenter)
        ) {
            AdditionalInfo(
                imageVector = Icons.Rounded.AccessTime,
                text = recipe.preparationTime
            )
            Spacer(modifier = Modifier.width(24.dp))
            AdditionalInfo(
                imageVector = Icons.Rounded.Person,
                text = recipe.numberOfDishes.toString()
            )
        }
    }
}

@Composable
private fun AdditionalInfo(imageVector: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = Color.Gray.copy(alpha = 0.8f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(id = R.string.generic_icon_description),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color.White)
    }
}

@Composable
private fun NameSection(recipe: Recipe) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = recipe.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = recipe.description)
    }
}

@Composable
private fun IngredientSection(ingredients: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.ingredients),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        ingredients.forEach { ingredient ->
            Text(text = ingredient, modifier = Modifier.padding(vertical = 12.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray, shape = DottedShape(step = 10.dp))
            )
        }
    }
}

@Composable
private fun PreparationSection(recipe: Recipe) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.preparation),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = recipe.preparation)
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    BaseScreen {
        ScreenContent(
            recipe = Recipe(
                id = "1",
                name = "Ceviche",
                description = "Originario del Perú",
                preparation = "Lavar el pescado...",
                preparationTime = "30 minutos",
                numberOfDishes = 4,
                ingredients = listOf("1 kg de Toyo", "Limón", "Sal al gusto")
            )
        ) {}
    }
}
