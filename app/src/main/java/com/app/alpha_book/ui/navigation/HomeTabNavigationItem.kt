package com.app.alpha_book.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class Tab{
    HOME_TAB,
    SEARCH_TAB,
    PROFILE_TAB,
    EXPLORE_TAB,
    ADD_BOOK_TAB
}
sealed class HomeTabItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : HomeTabItem(Tab.HOME_TAB.name, Icons.Filled.Home, "Home")
    object Search : HomeTabItem(Tab.SEARCH_TAB.name, Icons.Filled.Search, "Search")
    object Profile : HomeTabItem(Tab.PROFILE_TAB.name, Icons.Filled.Person, "Profile")
    object Explore : HomeTabItem(Tab.EXPLORE_TAB.name,Icons.Filled.Explore,"Explore")
    object AddBook : HomeTabItem(Tab.ADD_BOOK_TAB.name,Icons.Filled.Add,"AddBook")
}


