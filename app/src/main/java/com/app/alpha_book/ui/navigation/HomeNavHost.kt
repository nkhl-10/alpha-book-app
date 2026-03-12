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
import com.app.alpha_book.ui.viewModel.UserViewModel


@Composable
fun HomeNavHost(
    navController: NavHostController, 
    mainNavController: NavController, 
    viewModel: UserViewModel, 
    modifier: Modifier,
    startDestination: String = HomeTabItem.Home.route,
    profileTab: Int = 0
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable(HomeTabItem.Home.route) { HomeTabScreen(viewModel,mainNavController) }
        composable(HomeTabItem.Explore.route) { ExploreTabScreen(viewModel,mainNavController) }
        composable(HomeTabItem.Profile.route) { ProfileTabScreen(viewModel,mainNavController, profileTab) }
        composable(HomeTabItem.AddBook.route) { AddBookTabScreen(viewModel,navController,) }
    }
}