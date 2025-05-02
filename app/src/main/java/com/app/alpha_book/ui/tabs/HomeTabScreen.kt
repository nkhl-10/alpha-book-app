package com.app.alpha_book.ui.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.alpha_book.R
import com.app.alpha_book.base.MainActivity.Companion.latitude
import com.app.alpha_book.base.MainActivity.Companion.longitude
import com.app.alpha_book.model.Book
import com.app.alpha_book.model.Transaction
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import com.app.alpha_book.ui.utils.CenterLoadingView
import com.app.alpha_book.ui.utils.NoBookAvailable
import com.app.alpha_book.ui.viewModel.UserViewModel
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun HomeTabScreen(mainNavController: NavController) {
    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
    val bookList by viewModel.bookList.collectAsState()

    LaunchedEffect(Unit) { viewModel.getBookList() }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            bookList == null -> CenterLoadingView()
            bookList!!.isEmpty() -> NoBookAvailable()
            else -> BookList(list = bookList.orEmpty()){ bookId->
                mainNavController.navigate(ScreenNavigationItem.BookDetails.route + "/${bookId}/" + false)
            }
        }
    }
}


@Composable
fun BookList(list: List<Book>, click: (Int) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        columns = GridCells.Fixed(2),
    ) {
        items(list) {
            BookItems(it, click)
        }
    }
}

@Composable
fun BookItems(books: Book, click: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clickable { click(books.id) },
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        val bookLat = books.location?.latitude
        val bookLon = books.location?.longitude

        val distance = if (bookLat != null && bookLon != null) {
            "%.2f".format(calculateDistanceFromUser(bookLat, bookLon)) + " km"
        } else "Distance N/A"
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
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(4.dp),
                maxLines = 1
            )
            Text(
                text = "Price: ₹${books.price.toString()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp),
                maxLines = 1
            )
            Text(
                text = "Category Name: ${books.category?.name.toString()}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(4.dp),
                maxLines = 1
            )
            Text(
                text = "Location: ${books.location?.city.toString()}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(4.dp),
                maxLines = 1
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Distance Icon",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = distance,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

        }
    }
}

fun calculateDistanceFromUser(lat2: Double, lon2: Double): Double {
    val R = 6371 // Earth radius in km

    latitude =23.0686
        longitude=72.6536

    val dLat = Math.toRadians(lat2 - latitude)
    val dLon = Math.toRadians(lon2 - longitude)

    val a = sin(dLat / 2).pow(2.0) + cos(Math.toRadians(latitude)) *
            cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2.0)

    val c = 2 * asin(sqrt(a))
    return R * c
}

@Composable
fun BookListRowItem(
    transaction: Transaction,
    isSellerPage: Boolean = false,
    navController: NavController?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable {
                navController?.navigate(ScreenNavigationItem.TransactionScreen.route + "/${transaction.id}/" + "$isSellerPage")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Book Image - Left Side
            if (!transaction.book.images.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(transaction.book.images[0].imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Book Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.placeholder),
                    contentDescription = "Default Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }

            // Book Info - Right Side
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth()
            ) {
                Text(text = transaction.book.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "₹${transaction.book.price}", style = MaterialTheme.typography.bodyMedium)
                Text(text = transaction.book.category?.name ?: "", style = MaterialTheme.typography.bodySmall)
                Text(text = transaction.book.location?.city ?: "", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun BookListRow(
    list: List<Transaction>,
    isSellerPage: Boolean = false,
    navController: NavController?
) {
    LazyColumn {
        items(list) { BookListRowItem(it, isSellerPage, navController) }
    }

}
