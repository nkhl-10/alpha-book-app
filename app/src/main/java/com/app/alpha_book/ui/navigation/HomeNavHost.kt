package com.app.alpha_book.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.alpha_book.ui.tabs.AddBookTabScreen
import com.app.alpha_book.ui.tabs.ExploreTabScreen
import com.app.alpha_book.ui.tabs.HomeTabScreen
import com.app.alpha_book.ui.tabs.ProfileTabScreen
import com.app.alpha_book.ui.tabs.SearchTabScreen


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