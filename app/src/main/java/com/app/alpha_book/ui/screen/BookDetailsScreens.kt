package com.app.alpha_book.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.app.alpha_book.R
import com.app.alpha_book.model.Book
import com.app.alpha_book.model.BookImage
import com.app.alpha_book.model.Location
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.ui.navigation.Argument
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import com.app.alpha_book.ui.utils.ApiStatus
import com.app.alpha_book.ui.utils.CenterLoadingView
import com.app.alpha_book.ui.viewModel.UserViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun BookDetailsScreen(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bookId = navBackStackEntry?.arguments?.getInt(Argument.BOOK_ID.name)
    val isProfile = navBackStackEntry?.arguments?.getBoolean(Argument.IS_PROFILE.name)
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

    Scaffold(
        topBar =    {
            CenterAlignedTopAppBar(
                title = { Text(text = "Book Details") },
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
            val pagerState = rememberPagerState()
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                book?.let {
                    if (book?.status == ApiStatus.SUCCESS.code) {
                        val data = book!!.data
                        if (data!!.images!!.isNotEmpty()) BookImageCarousel(
                            images = data.images!!,
                            pagerState = pagerState
                        )
                        BookInfoSection(data)
                        LocationSection(data.location)

                        data.location?.let {
                            if (it.latitude != null && it.longitude != null) {
                                GoogleMapView(LatLng(it.latitude, it.longitude))
                            }
                        }

                    } else {
                        Text(
                            text = book!!.message.toString(),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                }
                if (viewModel.isLoading.value) CenterLoadingView()
            }
        },
        bottomBar = {
            if (!isProfile!!){
                if (bookId != book?.data?.sellerId) {
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate(ScreenNavigationItem.OrderPage.route + "/${bookId}")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                            ) { Text(text = "Buy Now", fontWeight = FontWeight.Bold) }
                        }
                    }
                }
            }

        }
    )

}

@Composable
fun BookImageCarousel(images: List<BookImage>, pagerState: PagerState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenWidthDp.dp)
    ) {
        HorizontalPager(count = images.size.coerceIn(1, images.size), state = pagerState) {
            AsyncImage(
                model = images[it].imageUrl,
                contentDescription = "Book Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder)
            )
        }

        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (images.size != 1) {
                repeat(images.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .padding(2.dp)
                            .background(
                                if (pagerState.currentPage == index) Color.Black else Color.Gray,
                                CircleShape
                            )
                    )
                }
            }

        }
    }
}


@Composable
fun BookInfoSection(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Author: ${book.author}",
                style = MaterialTheme.typography.bodyLarge
            )


            Text(
                text = "Price: ₹${book.price}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Condition: ${book.condition}",
                style = MaterialTheme.typography.bodyLarge
            )


            Text(
                text = "Description:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = book.description.toString(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun LocationSection(location: Location?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Seller Location",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "City: ${location?.city ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "State: ${location?.state ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Zip Code: ${location?.zipCode ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun GoogleMapView(latLng: LatLng) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latLng, 16f)
        }
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(state = MarkerState(position = latLng))
        }
    }

}
