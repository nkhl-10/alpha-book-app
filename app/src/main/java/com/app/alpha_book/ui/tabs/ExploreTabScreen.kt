package com.app.alpha_book.ui.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.alpha_book.model.Category
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import com.app.alpha_book.ui.utils.CenterLoadingView
import com.app.alpha_book.ui.viewModel.UserViewModel

@Composable
fun ExploreTabScreen(navController: NavController) {
    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
    val categoryList by viewModel.categoryList.collectAsState()

    LaunchedEffect(Unit) { viewModel.getCategoryList() }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            categoryList == null -> CenterLoadingView()
            categoryList!!.isEmpty() -> Text(text = "No books available")
            else -> CategoryList(list = categoryList.orEmpty(), navController)
        }
    }
}

@Composable
fun CategoryList(list: List<Category>, navController: NavController) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        columns = GridCells.Fixed(2)
    ) {
        items(list) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(
                            ScreenNavigationItem.CategoryDetails.route +
                                    "/${it.name}" + "/${it.id}"
                        )
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Text(
                    text = it.name ?: "N/A",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}