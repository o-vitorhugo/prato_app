package com.example.appprato.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.appprato.navigation.AppScreen
import com.example.appprato.ui.favoritos.FavoritosScreen
import com.example.appprato.ui.home.HomeScreen
import com.example.appprato.ui.minhasreceitas.CriarReceitasScreen
import com.example.appprato.ui.minhasreceitas.MinhasReceitasScreen
import com.example.appprato.ui.minhasreceitas.MinhasReceitasViewModel
import com.example.appprato.ui.notificacoes.NotificacoesScreen
import com.example.appprato.ui.perfil.PerfilScreen

sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarScreen("home_main", "Início", Icons.Default.Home)
    object Notificacoes : BottomBarScreen("notificacoes_main", "Notificações", Icons.Default.Notifications)
    object Favoritos : BottomBarScreen("favoritos_main", "Favoritos", Icons.Default.Favorite)
    object Perfil : BottomBarScreen("perfil_main", "Perfil", Icons.Default.Person)
}

object MinhasReceitasDestinations {
    const val GRAPH_ROUTE = "minhas_receitas_graph"
    const val LIST_ROUTE = "minhas_receitas_list"
    const val CREATE_ROUTE = "minhas_receitas_create"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val bottomBarNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            val items = listOf(
                BottomBarScreen.Home,
                BottomBarScreen.Notificacoes,
                BottomBarScreen.Favoritos,
                BottomBarScreen.Perfil
            )
            val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            bottomBarNavController.navigate(screen.route) {
                                popUpTo(bottomBarNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomBarNavController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomBarScreen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(BottomBarScreen.Notificacoes.route) {
                NotificacoesScreen(navController = navController)
            }
            composable(BottomBarScreen.Favoritos.route) {
                FavoritosScreen(navController = navController)
            }
            composable(BottomBarScreen.Perfil.route) {
                PerfilScreen(navController = bottomBarNavController)
            }
            navigation(
                startDestination = MinhasReceitasDestinations.LIST_ROUTE,
                route = MinhasReceitasDestinations.GRAPH_ROUTE
            ) {
                composable(MinhasReceitasDestinations.LIST_ROUTE) {
                    val parentEntry = remember(it) {
                        bottomBarNavController.getBackStackEntry(MinhasReceitasDestinations.GRAPH_ROUTE)
                    }
                    val viewModel = hiltViewModel<MinhasReceitasViewModel>(parentEntry)
                    MinhasReceitasScreen(
                        viewModel = viewModel,
                        onNavigateToCreate = { bottomBarNavController.navigate(MinhasReceitasDestinations.CREATE_ROUTE) },
                        onNavigateToDetails = { recipeId ->
                            navController.navigate(AppScreen.RecipeDetails.createRoute(recipeId))
                        }
                    )
                }
                composable(MinhasReceitasDestinations.CREATE_ROUTE) {
                    val parentEntry = remember(it) {
                        bottomBarNavController.getBackStackEntry(MinhasReceitasDestinations.GRAPH_ROUTE)
                    }
                    val viewModel = hiltViewModel<MinhasReceitasViewModel>(parentEntry)
                    CriarReceitasScreen(
                        viewModel = viewModel,
                        onNavigateBack = { bottomBarNavController.navigateUp() }
                    )
                }
            }
        }
    }
}
