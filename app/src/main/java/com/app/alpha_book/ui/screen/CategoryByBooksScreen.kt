package com.app.alpha_book.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.ui.navigation.Argument
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import com.app.alpha_book.ui.tabs.BookList
import com.app.alpha_book.ui.utils.CenterLoadingView
import com.app.alpha_book.ui.utils.NoBookAvailable
import com.app.alpha_book.ui.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryByBooks(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val categoryId = navBackStackEntry?.arguments?.getInt(Argument.CATEGORY_ID.name)
    val categoryName = navBackStackEntry?.arguments?.getString(Argument.CATEGORY_NAME.name)
    if (categoryId == null) {
        Text(text = "Invalid Category ID")
        return
    }

    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
    val bookList by viewModel.bookList.collectAsState()
    LaunchedEffect(categoryId) {
        viewModel.getCategoryList(categoryId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Category Name: $categoryName") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                when {
                    bookList == null -> CenterLoadingView()
                    bookList!!.isEmpty() -> NoBookAvailable()
                    else -> BookList(list = bookList.orEmpty(), viewModel, true){ bookId ->
                        navController.navigate(ScreenNavigationItem.BookDetails.route + "/${bookId}/" + false)
                    }
                }
            }
        },
    )
}