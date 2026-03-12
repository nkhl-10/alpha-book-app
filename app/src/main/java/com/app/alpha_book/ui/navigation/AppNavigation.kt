package com.app.alpha_book.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.app.alpha_book.ui.screen.LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.alpha_book.ui.screen.BookDetailsScreen
import com.app.alpha_book.ui.screen.CategoryByBooks
import com.app.alpha_book.ui.screen.HomeScreen
import com.app.alpha_book.ui.screen.OrderScreen
import com.app.alpha_book.ui.screen.RegisterScreen
import com.app.alpha_book.ui.screen.SplashScreen
import com.app.alpha_book.ui.screen.TransactionScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = ScreenNavigationItem.Splash.route
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(
            route = ScreenNavigationItem.Home.route + "?tab={tab}&profileTab={profileTab}",
            arguments = listOf(
                navArgument("tab") { 
                    type = NavType.StringType
                    defaultValue = HomeTabItem.Home.route
                },
                navArgument("profileTab") { 
                    type = NavType.IntType
                    defaultValue = 0 
                }
            )
        ) { backStackEntry ->
            val tab = backStackEntry.arguments?.getString("tab") ?: HomeTabItem.Home.route
            val profileTab = backStackEntry.arguments?.getInt("profileTab") ?: 0
            HomeScreen(navController, tab, profileTab)
        }

        composable(ScreenNavigationItem.Login.route) { LoginScreen(navController) }

        composable(ScreenNavigationItem.Splash.route) { SplashScreen(navController) }

        composable(ScreenNavigationItem.Register.route) { RegisterScreen(navController) }

        composable(
            route = ScreenNavigationItem.BookDetails.route + "/{${Argument.BOOK_ID.name}}"+ "/{${Argument.IS_PROFILE.name}}",
            arguments = listOf(
                navArgument(Argument.BOOK_ID.name) { type = NavType.IntType },
                navArgument(Argument.IS_PROFILE.name) { type = NavType.BoolType }
            )
        ) {
            it.arguments?.getInt(Argument.BOOK_ID.name)
            it.arguments?.getBoolean(Argument.IS_PROFILE.name)
            BookDetailsScreen(navController)
        }

        composable(
            route = ScreenNavigationItem.TransactionScreen.route + "/{${Argument.TRANSACTION_ID.name}}"+"/{${Argument.IS_SELLER_PAGE.name}}",
            arguments = listOf(
                navArgument(Argument.TRANSACTION_ID.name) { type = NavType.IntType },
                navArgument(Argument.IS_SELLER_PAGE.name) { type = NavType.BoolType },
            )
        ) {
            it.arguments?.getInt(Argument.TRANSACTION_ID.name)
            it.arguments?.getBoolean(Argument.IS_SELLER_PAGE.name)
            TransactionScreen(navController)
        }

        composable(
            route = ScreenNavigationItem.OrderPage.route +"/{${Argument.BOOK_ID.name}}",
            arguments = listOf(navArgument(Argument.BOOK_ID.name) { type = NavType.IntType })
        ) {
            it.arguments?.getInt(Argument.BOOK_ID.name)
            OrderScreen(navController)
        }

        composable(
            route = ScreenNavigationItem.CategoryDetails.route + "/{${Argument.CATEGORY_NAME.name}}/{${Argument.CATEGORY_ID.name}}",
            arguments = listOf(
                navArgument(Argument.CATEGORY_NAME.name) { type = NavType.StringType },
                navArgument(Argument.CATEGORY_ID.name) { type = NavType.IntType }
            )
        ) {
            it.arguments?.getString(Argument.CATEGORY_NAME.name) ?: ""
            it.arguments?.getInt(Argument.CATEGORY_ID.name) ?: 0
            CategoryByBooks(navController)
        }

    }
}

