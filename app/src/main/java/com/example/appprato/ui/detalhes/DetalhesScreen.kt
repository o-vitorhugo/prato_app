package com.example.appprato.ui.detalhes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appprato.data.local.RecipeEntity
import com.example.appprato.navigation.AppScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetalhesScreen(
    navController: NavController,
    viewModel: DetalhesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    uiState.substitutionSuggestion?.let {
        AlertDialog(
            onDismissRequest = viewModel::clearSuggestion,
            title = { Text("Sugestão de Substituição") },
            text = { Text(it) },
            confirmButton = {
                TextButton(onClick = viewModel::clearSuggestion) {
                    Text("OK")
                }
            }
        )
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.recipe?.name ?: "", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    uiState.recipe?.let {
                        IconButton(onClick = { navController.navigate(AppScreen.Notifications.route) }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificações")
                        }
                        if (uiState.isOwnedByUser) { // Mostra o botão apenas se o usuário for o dono
                            IconButton(onClick = { navController.navigate(AppScreen.EditRecipe.createRoute(it.id)) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar Receita")
                            }
                        }
                        IconToggleButton(checked = it.isFavorite, onCheckedChange = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (it.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favoritar"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.recipe != null) {
                val recipe = uiState.recipe!!
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        AsyncImage(
                            model = recipe.imageUrl,
                            contentDescription = recipe.name,
                            modifier = Modifier.fillMaxWidth().height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(recipe.description, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                            if (recipe.tags.isNotEmpty()) {
                                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    recipe.tags.forEach { tag ->
                                        AssistChip(onClick = { /* No-op */ }, label = { Text(tag) })
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            DetailTabs(recipe = recipe, onIngredientClick = viewModel::onIngredientClicked)
                        }
                    }
                }
            } else {
                Text("Receita não encontrada.")
            }
        }
    }
}

@Composable
fun DetailTabs(recipe: RecipeEntity, onIngredientClick: (String) -> Unit) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Ingredientes", "Modo de Preparo")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(title, style = MaterialTheme.typography.titleSmall) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (tabIndex) {
            0 -> IngredientsList(recipe, onIngredientClick)
            1 -> PreparationList(recipe)
        }
    }
}

@Composable
fun IngredientsList(recipe: RecipeEntity, onIngredientClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            recipe.ingredients.forEach { ingredient ->
                Text(
                    text = ingredient,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onIngredientClick(ingredient) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}

@Composable
fun PreparationList(recipe: RecipeEntity) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        recipe.preparationSteps.forEachIndexed { index, step ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "${index + 1}.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(step, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
