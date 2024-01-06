package com.angelhr28.yapechallenge2024.ui.features.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.angelhr28.domain.model.Recipe
import com.angelhr28.yapechallenge2024.R
import com.angelhr28.yapechallenge2024.ui.components.BaseScreen
import com.angelhr28.yapechallenge2024.ui.components.ErrorComponent
import com.angelhr28.yapechallenge2024.ui.components.LoadingComponent

@Composable
fun HomeScreen(
    viewModel: HomeListViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        // Cargar recetas al iniciar
        viewModel.handleUiEvent(UiEvent.LoadRecipes)
    }

    when {
        uiState.isLoading -> LoadingComponent(modifier = Modifier.fillMaxSize())
        else -> ScreenContent(uiState = uiState, sendUiEvent = viewModel::handleUiEvent)
    }

    uiState.error?.let { error ->
        ErrorComponent(
            error = error,
            onDismiss = { viewModel.handleUiEvent(UiEvent.ErrorShown) }
        )
    }

    uiState.selectedRecipeId?.let { selectedId ->
        onNavigateToDetail(selectedId)
        viewModel.handleUiEvent(UiEvent.DetailShown)
    }
}

@Composable
private fun ScreenContent(uiState: UiState, sendUiEvent: (UiEvent) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row {
            var textToSearch by remember { mutableStateOf(uiState.textToSearch) }

            TextField(
                value = textToSearch,
                onValueChange = {
                    textToSearch = it
                    sendUiEvent(UiEvent.Search(textToSearch))
                },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(id = R.string.search)) },
                singleLine = true,
                trailingIcon = {
                    if (textToSearch.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(id = R.string.clear_text_description),
                            modifier = Modifier.clickable {
                                textToSearch = String()
                                sendUiEvent(UiEvent.Search(textToSearch))
                            }
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
                onClick = { sendUiEvent(UiEvent.RefreshRecipes) },
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Autorenew,
                    contentDescription = stringResource(id = R.string.refresh_icon_description),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = uiState.recipes,
                key = { recipe -> recipe.id }
            ) { recipe ->
                Card(modifier = Modifier.clickable { sendUiEvent(UiEvent.RecipeItemClick(recipe.id)) }) {
                    AsyncImage(
                        model = recipe.imageUrl,
                        contentDescription = recipe.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.aspectRatio(4f / 3f)
                    )
                    Text(
                        text = recipe.name,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(8.dp)
                    )
                    Row(Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                        AdditionalInfo(
                            imageVector = Icons.Rounded.AccessTime,
                            text = recipe.preparationTime,
                            modifier = Modifier.weight(0.7f)
                        )
                        AdditionalInfo(
                            imageVector = Icons.Rounded.Person,
                            text = recipe.numberOfDishes.toString(),
                            modifier = Modifier.weight(0.3f)
                        )
                    }
                }
            }
        }

        if (uiState.recipes.isEmpty()) {
            Text(
                text = stringResource(id = R.string.empty_result),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 150.dp)
            )
        }
    }
}

@Composable
private fun AdditionalInfo(imageVector: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(id = R.string.generic_icon_description),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    BaseScreen {
        ScreenContent(
            UiState(
                recipes = listOf(
                    Recipe(
                        id = "1",
                        name = "Ceviche",
                        preparationTime = "1 horas",
                        numberOfDishes = 5,
                    ),
                    Recipe(
                        id = "2",
                        name = "Arroz con pollo",
                        preparationTime = "2 horas",
                        numberOfDishes = 3,
                    )
                )
            )
        ) {}
    }
}
