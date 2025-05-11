package com.example.gamedealshunter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamedealshunter.ui.deals.DealsScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "deals") {
        composable("deals") { DealsScreen() }
    }
}
