package com.app.alpha_book.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.app.alpha_book.R
import com.app.alpha_book.model.Address
import com.app.alpha_book.model.BuyReqModel
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.remote.sharedPreferences.USER
import com.app.alpha_book.remote.sharedPreferences.getIntData
import com.app.alpha_book.ui.navigation.Argument
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import com.app.alpha_book.ui.utils.ApiStatus
import com.app.alpha_book.ui.viewModel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(navController: NavController) {
    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
    val addressList by viewModel.addressListState.collectAsState()
    val book by viewModel.bookDetailsState.collectAsState()
    val userId = LocalContext.current.getIntData(USER.ID.name, 0)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bookId = navBackStackEntry?.arguments?.getInt(Argument.BOOK_ID.name)
    var selectedAddress by remember { mutableStateOf<Address?>(null) }

    LaunchedEffect(Unit) {
        if (bookId != null) {
            viewModel.getBookList(bookId)
        }
        viewModel.getAddress(userId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Order Page", fontWeight = FontWeight.Bold) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Select Address", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                addressList.let {
                    it?.forEach { address ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedAddress = address }
                                .padding(4.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedAddress == address,
                                    onClick = { selectedAddress = address }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${address.street}, ${address.city}, ${address.state}, ${address.zip_code}",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                } ?: Text(text = "No addresses available", fontSize = 16.sp)


                book?.data.let { book ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row {
                            book?.images?.get(0).let { url->
                                AsyncImage(
                                    model = url?.imageUrl,
                                    contentDescription = "Book Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(100.dp),
                                    placeholder = painterResource(R.drawable.placeholder),
                                    error = painterResource(R.drawable.placeholder)
                                )
                            }
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Book Title: ${book?.title}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = "Author: ${book?.author}", fontSize = 16.sp)
                                Text(
                                    text = "Price: ₹${book?.price}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Book Address", fontSize = 18.sp, fontWeight = FontWeight.Bold)


                book?.data?.location.let { address ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "Street: ${address?.street}", fontSize = 18.sp)
                            Text(text = "City: ${address?.city}", fontSize = 16.sp)
                            Text(text = "State: ${address?.state}", fontSize = 16.sp)
                            Text(text = "Zip Code: ${address?.zipCode}", fontSize = 16.sp)
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (bookId != book?.data?.sellerId) BuyButtons(
                bookId = bookId ?: 0,
                amount = book?.data?.price ?: 0.0,
                navController = navController,
                selectedAddress = selectedAddress
            )
        }
    )
}

@Composable
fun BuyButtons(
    bookId: Int,
    amount: Double,
    navController: NavController,
    selectedAddress: Address?
) {
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
    val userId = LocalContext.current.getIntData(USER.ID.name, 0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (selectedAddress == null) {
                        Toast.makeText(context, "Please select an address", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }
                    isLoading = true
                    viewModel.viewModelScope.launch {
                        val data = BuyReqModel(userId, bookId, amount)
                        val response = viewModel.buyBook(data)
                        isLoading = false
                        if (response.status == ApiStatus.CREATED.code) {
                            delay(2000)
                            navController.navigate(ScreenNavigationItem.Home.route) {
                                popUpTo(ScreenNavigationItem.OrderPage.route) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Failed to buy", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(text = "Confirm Order", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}