package com.app.alpha_book.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.app.alpha_book.ui.screen.LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.alpha_book.ui.screen.BookDetailsScreen
import com.app.alpha_book.ui.screen.HomeScreen
import com.app.alpha_book.ui.screen.ProfileScreen
import com.app.alpha_book.ui.screen.RegisterScreen
import com.app.alpha_book.ui.screen.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String = NavigationItem.Splash.route) {
    NavHost(navController = navController, startDestination = startDestination) {

     /*   composable(
            route = NavigationItem.Home.route+  "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            val userId = it.arguments?.getInt(USER.USER_ID.name)
            HomeScreen(navController)
        }*/

        composable(NavigationItem.Home.route){ HomeScreen(navController) }

        composable(NavigationItem.Login.route){LoginScreen(navController)}

        composable(NavigationItem.Profile.route) { ProfileScreen(navController) }

        composable(NavigationItem.Splash.route) { SplashScreen(navController) }

        composable(NavigationItem.Register.route) { RegisterScreen(navController) }

        composable(
            route = NavigationItem.BookDetails.route+"/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) {
            val bookId = it.arguments?.getInt("bookId")
            BookDetailsScreen(navController)
        }

    }
}

