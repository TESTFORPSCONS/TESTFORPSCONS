package com.example.testarcanitonefile.ui

import androidx.navigation.compose.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "search") {
        composable("search") {
            SearchScreen(navController = navController)
        }
        composable("repositoryContent/{owner}/{repo}") { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            RepositoryContentScreen(owner = owner, repo = repo, navController = navController)
        }
        composable("webview/{url}") { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            WebViewScreen(url = url, navController)
        }
    }
}

