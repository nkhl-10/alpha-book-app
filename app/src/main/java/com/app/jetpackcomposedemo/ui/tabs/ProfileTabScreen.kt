package com.app.jetpackcomposedemo.ui.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.jetpackcomposedemo.R
import com.app.jetpackcomposedemo.model.Book
import com.app.jetpackcomposedemo.model.User
import com.app.jetpackcomposedemo.remote.api.ApiImpl
import com.app.jetpackcomposedemo.remote.api.ApiInterface
import com.app.jetpackcomposedemo.ui.viewModel.UserViewModel

@Composable
fun ProfileTabScreen(navController: NavController) {
    /* Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
         Button(onClick = {
             context.clearAllData()
             navHostController.navigate(NavigationItem.Splash.route)
         }) {}*/
//            Text(text = "Logout", style = TextStyle(fontSize = 18.sp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        UserProfileCard(
            User(0, "Nkhl", "nikboy@gmail.com", "000", "9530301242", "aaaaa", true, 0, 0)
        )
        BooksPager(navController)
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
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
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
                user.phone?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Button(onClick = {}) {
                    Text("Edit Profile")
                }
            }
        }
    }
}

@Composable
fun BooksPager(navController: NavController) {
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

                    LaunchedEffect(Unit) { viewModel.getUserByBookList(1) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        when {
                            bookList == null -> Text(text = "Loading...", modifier = Modifier.align(Alignment.Center))
                            bookList!!.isEmpty() -> Text(text = "No books available", modifier = Modifier.align(Alignment.Center))
                            else -> BookList(list = bookList.orEmpty(), navController)
                        }
                    }
                }

                1 -> {
                    val bookList by viewModel.bookList.collectAsState()

                    LaunchedEffect(Unit) { viewModel.getOrderedByBookList(1) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        when {
                            bookList == null -> Text(text = "Loading...", modifier = Modifier.align(Alignment.Center))
                            bookList!!.isEmpty() -> Text(text = "No books available", modifier = Modifier.align(Alignment.Center))
                            else -> BookList(list = bookList.orEmpty(), navController)
                        }
                    }
                }
            }
        }
    }
}
