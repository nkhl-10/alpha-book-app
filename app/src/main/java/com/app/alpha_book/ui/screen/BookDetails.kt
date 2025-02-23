package com.app.alpha_book.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.app.alpha_book.model.Book
import com.app.alpha_book.model.BookImage
import com.app.alpha_book.model.Category
import com.app.alpha_book.model.Location
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.ui.utils.ApiStatus
import com.app.alpha_book.ui.viewModel.UserViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@Composable
@Preview
fun demoPreview() {
    val book = listOf(
        Book(
            id = 1,
            images = emptyList(),
            category = Category(id = 1, name = "book", description = "book demo"),
            location = Location(
                id = 1, user = 10, street = "d", city = "d", state = "d",
                zipCode = "d", latitude = null, longitude = null
            ),
            title = "Book Title 1",
            author = "Author 1",
            description = "Description of bookDescription of bookDescription of bookDescription of bookDescription of bookDescription of bookDescription of bookDescription of book 1",
            price = "123.00",
            condition = "Good",
            bookType = "Resell",
            pdfFile = null,
            readAccess = null,
            createdAt = "2025-02-16T15:20:42.886990Z",
            updatedAt = "2025-02-16T15:20:42.887035Z",
            sellerId = 10
        )
    )

    Column {
        val data = book[0]
        if (data.images!!.isNotEmpty()) BookImageCarousel(
            images = data.images,
            pagerState = PagerState(0)
        )
        BookInfoSection(data)
        Spacer(modifier = Modifier.height(16.dp))
        BuyButtons()
    }


}


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
    LaunchedEffect(bookId) {
        viewModel.getBookList(bookId)
    }

    val pagerState = rememberPagerState()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(4.dp)
    ) {
        book?.let {
            if (book?.status == ApiStatus.SUCCESS.code) {
                val data = book!!.data
                if (data!!.images!!.isNotEmpty()) BookImageCarousel(
                    images = data.images!!,
                    pagerState = pagerState
                )
                BookInfoSection(data)
                Spacer(modifier = Modifier.height(16.dp))
                BuyButtons()
            } else {
                Text(
                    text = book!!.message.toString(),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

        } ?: CircularProgressIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun BookImageCarousel(images: List<BookImage>, pagerState: PagerState) {
    Box(modifier = Modifier.fillMaxWidth().height(500.dp)) {
        HorizontalPager(count = images.size.coerceIn(1, images.size), state = pagerState) { page ->
            AsyncImage(
                model = /*images[page]*/"https://picsum.photos/200/500",
                contentDescription = "Book Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .padding(2.dp)
                        .background(if (pagerState.currentPage == index) Color.Black else Color.Gray, CircleShape)
                )
            }
        }
    }
}


@Composable
fun BookInfoSection(book: Book) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = book.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Text(text = "Author: ${book.author}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Price: ₹${book.price}", style = MaterialTheme.typography.labelLarge)
        Text(text = "Condition: ${book.condition}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Description:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(text = book.description.toString(), style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LocationSection(book.location)
    }
}

@Composable
fun LocationSection(location: Location?) {
    Column {
        Text(text = "Seller Location", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(text = "City: ${location?.city ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "State: ${location?.state ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Zip Code: ${location?.zipCode ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
    }
}
@Composable
fun BuyButtons() {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(
            onClick = { /* Handle Add to Cart */ },
            colors = ButtonDefaults.buttonColors(Color.Yellow)
        ) {
            Text(text = "Add to Cart", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = { /* Handle Buy Now */ },
            colors = ButtonDefaults.buttonColors(Color.Red)
        ) {
            Text(text = "Buy Now", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
