package com.app.jetpackcomposedemo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.jetpackcomposedemo.ui.tabs.AddBookTabScreen
import com.app.jetpackcomposedemo.ui.tabs.ExploreTabScreen
import com.app.jetpackcomposedemo.ui.tabs.HomeTabScreen
import com.app.jetpackcomposedemo.ui.tabs.ProfileTabScreen
import com.app.jetpackcomposedemo.ui.tabs.SearchTabScreen


@Composable
fun HomeNavHost(navController: NavHostController,mainNavController: NavController,modifier: Modifier) {
    NavHost(navController = navController, startDestination = HomeTabItem.Home.route, modifier = modifier) {
        composable(HomeTabItem.Home.route) { HomeTabScreen(mainNavController) }
        composable(HomeTabItem.Search.route) { SearchTabScreen(mainNavController) }
        composable(HomeTabItem.Profile.route) { ProfileTabScreen(mainNavController) }
        composable(HomeTabItem.Explore.route) { ExploreTabScreen(mainNavController) }
        composable(HomeTabItem.AddBook.route) { AddBookTabScreen(mainNavController) }
    }
}