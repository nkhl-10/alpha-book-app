package com.app.alpha_book.ui.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.alpha_book.model.Category
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import com.app.alpha_book.ui.utils.CenterLoadingView
import com.app.alpha_book.ui.utils.NoBookAvailable
import com.app.alpha_book.ui.viewModel.UserViewModel

@Composable
fun ExploreTabScreen(viewModel: UserViewModel, navController: NavController) {

    val categoryList by viewModel.categoryList.collectAsState()
    val books by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getCategoryList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchBooks(query)
            },
            label = { Text("Search...") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        when {
            query.isNotEmpty() -> {
                when {
                    isLoading -> CircularProgressIndicator()
                    books.isEmpty() -> NoBookAvailable()
                    else -> LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(10.dp),
                        columns = GridCells.Fixed(2)
                    ) {
                        items(books) {
                            BookItems(it, true, viewModel) { bookId ->
                                navController.navigate(ScreenNavigationItem.BookDetails.route + "/${bookId}/" + false)
                            }
                        }
                    }
                }
            }
            categoryList == null -> CenterLoadingView()
            categoryList!!.isEmpty() -> NoBookAvailable()
            else -> CategoryList(
                list = categoryList.orEmpty(),
                navController = navController
            )
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
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}