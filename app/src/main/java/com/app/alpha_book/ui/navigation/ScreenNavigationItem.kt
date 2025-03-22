package com.app.alpha_book.ui.navigation



sealed class ScreenNavigationItem(val route: String) {
    object Home : ScreenNavigationItem(Screen.HOME.name)
    object Login : ScreenNavigationItem(Screen.LOGIN.name)
    object Splash : ScreenNavigationItem(Screen.SPLASH.name)
    object Register : ScreenNavigationItem(Screen.REGISTER.name)
    object BookDetails : ScreenNavigationItem(Screen.BOOK_DETAILS.name)
    object CategoryDetails : ScreenNavigationItem(Screen.CATEGORY_DETAILS.name)
    object OrderPage : ScreenNavigationItem(Screen.ORDER_PAGE.name)
}


