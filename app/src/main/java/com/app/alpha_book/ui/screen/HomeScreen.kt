package com.app.alpha_book.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.alpha_book.ui.components.AppBar
import com.app.alpha_book.ui.navigation.HomeNavHost
import com.app.alpha_book.ui.navigation.HomeTabItem
import com.app.alpha_book.ui.tabs.BottomNavigationBar


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(mainNav: NavController) {
    val navControllerBottom = rememberNavController()
    // Observe the current backstack entry as state
    val navBackStackEntry by navControllerBottom.currentBackStackEntryAsState()
    // Get the current route
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navControllerBottom) },
        topBar = {
            AppBar(
                title = when (currentRoute) {
                    HomeTabItem.Home.route -> "Home"
                    HomeTabItem.Search.route -> "Search"
                    HomeTabItem.AddBook.route -> "AddBook"
                    HomeTabItem.Explore.route -> "Explore"
                    HomeTabItem.Profile.route -> "Profile"
                    else -> "Demo App"
                }
            )
        }
    ) { paddingValues ->
        HomeNavHost(
            navController = navControllerBottom,
            mainNavController = mainNav,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

