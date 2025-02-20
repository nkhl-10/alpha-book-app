package com.app.jetpackcomposedemo.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.app.jetpackcomposedemo.model.Book
import com.app.jetpackcomposedemo.model.BookImage
import com.app.jetpackcomposedemo.model.Location
import com.app.jetpackcomposedemo.remote.api.ApiImpl
import com.app.jetpackcomposedemo.remote.api.ApiInterface
import com.app.jetpackcomposedemo.ui.viewModel.UserViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState


@Composable
fun BookDetailsScreen(navHostController: NavController) {

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val bookId = navBackStackEntry?.arguments?.getInt("bookId")
    if (bookId == null) {
        Text(text = "Invalid Book ID", color = Color.Red)
        return
    }
    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
    val book by viewModel.bookDetailsState.collectAsState()
    LaunchedEffect(bookId) { viewModel.getBookList(bookId) }

    val pagerState = rememberPagerState()
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        book?.let {
            BookImageCarousel(images = book!![0].images!!, pagerState = pagerState)
            BookInfoSection(book!![0])
        }?: CircularProgressIndicator()
    }
}

@Composable
fun BookImageCarousel(images: List<BookImage>, pagerState: PagerState) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)) {
        HorizontalPager(count = images.size.coerceIn(1, 10), state = pagerState) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Book Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun BookInfoSection(book: Book) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = book.title, style = MaterialTheme.typography.headlineSmall, color = Color.Black)
        Text(text = "Author: ${book.author}", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "Price: ₹${book.price}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Red
        )
        Text(text = "Condition: ${book.condition}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Description: ${book.description}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LocationSection(book.location)
    }
}

@Composable
fun LocationSection(location: Location?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Location", style = MaterialTheme.typography.titleMedium)
        Text(text = "Street: ${location?.street ?: "N/A"}")
        Text(text = "City: ${location?.city ?: "N/A"}")
        Text(text = "State: ${location?.state ?: "N/A"}")
        Text(text = "Zip Code: ${location?.zipCode ?: "N/A"}")
    }
}