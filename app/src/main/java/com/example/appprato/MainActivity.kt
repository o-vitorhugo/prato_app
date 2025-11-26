package com.example.appprato

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appprato.navigation.AppNavGraph
import com.example.appprato.navigation.AppScreen
import com.example.appprato.ui.components.BottomNavBar
import com.example.appprato.ui.theme.AppPratoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val systemIsDark = isSystemInDarkTheme()
            var isDarkMode by rememberSaveable { mutableStateOf(systemIsDark) }

            AppPratoTheme(darkTheme = isDarkMode) {
                Scaffold(
                    bottomBar = {
                        // A lógica para mostrar/esconder a barra de navegação é puramente de UI
                        if (shouldShowBottomBar(navController)) {
                            BottomNavBar(navController = navController)
                        }
                    }
                ) { paddingValues ->
                    // O AppNavGraph será limpo a seguir para remover toda a lógica
                    AppNavGraph(
                        navController = navController,
                        isDarkMode = isDarkMode,
                        onThemeChange = { isDarkMode = it },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
private fun shouldShowBottomBar(navController: NavController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // A rota atual é usada para decidir se a barra inferior deve ser mostrada. Isso é lógica de UI.
    val currentRoute = navBackStackEntry?.destination?.route
    return when (currentRoute) {
        AppScreen.Home.route,
        AppScreen.Favorites.route,
        AppScreen.Profile.route -> true
        else -> false
    }
}
