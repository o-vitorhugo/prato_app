package com.example.appprato.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appprato.ui.login.LoginScreen

/**
 * Ponto de partida. Gráfico de navegação zerado.
 * A única rota conhecida é o Login.
 */
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            // Por enquanto, a tela de login não faz nada, apenas é exibida.
            LoginScreen(navController)
        }

        // Outras telas serão adicionadas aqui, passo a passo.
    }
}
