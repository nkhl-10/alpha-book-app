package com.app.jetpackcomposedemo.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.app.jetpackcomposedemo.ui.screen.LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.jetpackcomposedemo.remote.sharedPreferences.USER
import com.app.jetpackcomposedemo.ui.screen.HomeScreen
import com.app.jetpackcomposedemo.ui.screen.ProfileScreen
import com.app.jetpackcomposedemo.ui.screen.RegisterScreen
import com.app.jetpackcomposedemo.ui.screen.SplashScreen

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

        composable(NavigationItem.Profile.route) { ProfileScreen(navController ) }

        composable(NavigationItem.Splash.route) { SplashScreen(navController) }

        composable(NavigationItem.Register.route) { RegisterScreen(navController) }


    }
}

