package com.app.alpha_book.ui.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.alpha_book.R
import com.app.alpha_book.model.Book
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import com.app.alpha_book.ui.viewModel.UserViewModel

@Composable
fun HomeTabScreen(mainNavController: NavController) {
    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
    val bookList by viewModel.bookList.collectAsState()

    LaunchedEffect(Unit) { viewModel.getBookList() }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            bookList == null -> Text(text = "Loading...")
            bookList!!.isEmpty() -> Text(text = "No books available")
            else -> BookList(list = bookList.orEmpty(), mainNavController)
        }
    }
}


@Composable
fun BookList(list: List<Book>, navController: NavController) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        columns = GridCells.Fixed(2)
    ) {
        items(list) {
            BookItems(it, navController)
        }
    }
}

@Composable
fun BookItems(books: Book, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clickable {
                navController.navigate(ScreenNavigationItem.BookDetails.route+"/${books.id}")
            },
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            if (books.images!!.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(books.images[0].imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Book Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.placeholder), // ✅ Default placeholder
                    contentDescription = "Default Book Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }
            Text(
                text = books.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Price: $${books.price.toString()}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Category Name: ${books.category?.name.toString()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Location: ${books.location?.city.toString()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
