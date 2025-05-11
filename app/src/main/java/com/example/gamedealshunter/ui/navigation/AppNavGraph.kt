package com.example.gamedealshunter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamedealshunter.ui.deals.DealsScreen
import com.example.gamedealshunter.ui.settings.SettingsScreen


@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "deals"
    ) {
        composable("deals")    { DealsScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}
