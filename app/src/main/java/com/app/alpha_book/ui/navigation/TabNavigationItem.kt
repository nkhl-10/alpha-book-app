package com.app.alpha_book.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector


sealed class HomeTabItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : HomeTabItem(Tab.HOME_TAB.name, Icons.Filled.Home, "Home")
    object Profile : HomeTabItem(Tab.PROFILE_TAB.name, Icons.Filled.Person, "Profile")
    object Explore : HomeTabItem(Tab.EXPLORE_TAB.name,Icons.Filled.Explore,"Explore")
    object AddBook : HomeTabItem(Tab.ADD_BOOK_TAB.name,Icons.Filled.Add,"AddBook")
}


