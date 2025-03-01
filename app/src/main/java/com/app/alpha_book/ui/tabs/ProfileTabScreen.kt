package com.app.alpha_book.ui.tabs

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.alpha_book.R
import com.app.alpha_book.model.User
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.remote.sharedPreferences.USER
import com.app.alpha_book.remote.sharedPreferences.getIntData
import com.app.alpha_book.ui.utils.ApiStatus
import com.app.alpha_book.ui.viewModel.UserViewModel

@Composable
fun ProfileTabScreen(navController: NavController) {
    val context = LocalContext.current
    /* Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
         Button(onClick = {
             context.clearAllData()
             navHostController.navigate(NavigationItem.Splash.route)
         }) {}*/
//            Text(text = "Logout", style = TextStyle(fontSize = 18.sp))


    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
    val user by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        val id = context.getIntData(USER.ID.name, 0)
        viewModel.getUser(id)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        if (user?.status == ApiStatus.SUCCESS.code) {
            user?.data?.let {
                UserProfileCard(it)
                BooksPager(navController, it.id)
            } ?: Text("Loading...")

        } else {
            Text(text = "Try Again!!")
        }

    }
}


@Composable
fun UserProfileCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatar,
                contentDescription = "Book Image",
                contentScale = ContentScale.Crop, modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.Top)
                    .fillMaxWidth()
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = user.phone ?: "Not Available Phone No",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {},
                content = { Text("Edit Profile") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(18.dp))

            OutlinedButton(
                onClick = {},
                content = { Text("Add Book") },
                modifier = Modifier.weight(1f)
            )


        }
    }
}

@Composable
fun BooksPager(navController: NavController, id: Int) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabTitles = listOf("Your Books", "Ordered Books")
    TabRow(selectedTabIndex = pagerState.currentPage) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = { pagerState.getOffsetDistanceInPages(index) }
            )
        }
    }
    Column {
        HorizontalPager(state = pagerState) { page ->


            val userApi: ApiInterface = ApiImpl()
            val viewModel = remember { UserViewModel(userApi) }
            when (page) {
                0 -> {
                    val bookList by viewModel.bookList.collectAsState()

                    LaunchedEffect(Unit) { viewModel.getUserByBookList(id) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        when {
                            bookList == null -> Text(
                                text = "Loading...",
                                modifier = Modifier.align(Alignment.Center)
                            )

                            bookList!!.isEmpty() -> Text(
                                text = "No books available",
                                modifier = Modifier.align(Alignment.Center)
                            )

                            else -> BookList(list = bookList.orEmpty(), navController)
                        }
                    }
                }

                1 -> {
                    val bookList by viewModel.bookList.collectAsState()

                    LaunchedEffect(Unit) { viewModel.getOrderedByBookList(id) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        when {
                            bookList == null -> Text(
                                text = "Loading...",
                                modifier = Modifier.align(Alignment.Center)
                            )

                            bookList!!.isEmpty() -> Text(
                                text = "No books available",
                                modifier = Modifier.align(Alignment.Center)
                            )

                            else -> BookList(list = bookList.orEmpty(), navController)
                        }
                    }
                }
            }
        }
    }
}
