package com.example.appprato.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appprato.ui.detalhes.DetalhesScreen
import com.example.appprato.ui.favoritos.FavoritosScreen
import com.example.appprato.ui.home.HomeScreen
import com.example.appprato.ui.login.CadastroScreen
import com.example.appprato.ui.login.LoginScreen
import com.example.appprato.ui.minhasreceitas.EditarReceitaScreen
import com.example.appprato.ui.minhasreceitas.MinhasReceitasScreen
import com.example.appprato.ui.notifications.NotificationsScreen
import com.example.appprato.ui.perfil.ConfiguracoesScreen
import com.example.appprato.ui.perfil.GerenciarPerfilScreen
import com.example.appprato.ui.perfil.PerfilScreen

sealed class AppScreen(val route: String) {
    object Login : AppScreen("login")
    object Cadastro : AppScreen("cadastro")
    object Home : AppScreen("home")
    object Favorites : AppScreen("favorites")
    object Profile : AppScreen("profile")
    object Settings : AppScreen("settings")
    object Notifications : AppScreen("notifications")
    object GerenciarPerfil : AppScreen("gerenciar_perfil")
    object RecipeDetails : AppScreen("recipeDetails/{recipeId}") {
        fun createRoute(recipeId: String) = "recipeDetails/$recipeId"
    }
    object MyRecipes : AppScreen("myRecipes")
    object EditRecipe : AppScreen("editRecipe?recipeId={recipeId}") {
        fun createRoute(recipeId: String?) = if (recipeId != null) "editRecipe?recipeId=$recipeId" else "editRecipe"
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    val startDestination = when (authState) {
        AuthState.LOGGED_IN -> AppScreen.Home.route
        AuthState.LOGGED_OUT -> AppScreen.Login.route
        AuthState.LOADING -> "loading" // Rota temporária de carregamento
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("loading") { /* Tela de carregamento */ }
        composable(AppScreen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(AppScreen.Cadastro.route) {
            CadastroScreen(navController = navController)
        }
        composable(AppScreen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(AppScreen.Favorites.route) {
            FavoritosScreen(navController = navController)
        }
        composable(AppScreen.Profile.route) {
            PerfilScreen(navController = navController)
        }
        composable(AppScreen.Settings.route) {
            ConfiguracoesScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                onDarkModeChange = onThemeChange
            )
        }
        composable(AppScreen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(AppScreen.GerenciarPerfil.route) {
            GerenciarPerfilScreen(navController = navController)
        }
        composable(
            route = AppScreen.RecipeDetails.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) {
            DetalhesScreen(navController = navController)
        }
        composable(AppScreen.MyRecipes.route) {
             MinhasReceitasScreen(
                onNavigateToCreate = { navController.navigate(AppScreen.EditRecipe.createRoute(null)) },
                onNavigateToDetails = { recipeId -> navController.navigate(AppScreen.RecipeDetails.createRoute(recipeId)) }
            )
        }
        composable(
            route = AppScreen.EditRecipe.route,
            arguments = listOf(navArgument("recipeId") { nullable = true })
        ) {
            EditarReceitaScreen(navController = navController)
        }
    }
}
