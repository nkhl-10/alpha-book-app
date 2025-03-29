package com.app.alpha_book.ui.tabs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app.alpha_book.R
import com.app.alpha_book.model.Address
import com.app.alpha_book.model.User
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.remote.sharedPreferences.USER
import com.app.alpha_book.remote.sharedPreferences.getIntData
import com.app.alpha_book.ui.navigation.HomeTabItem
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import com.app.alpha_book.ui.utils.ApiStatus
import com.app.alpha_book.ui.viewModel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileOutputStream

@Composable
fun ProfileTabScreen(mainNavController: NavController,navController:NavController) {
    val context = LocalContext.current
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
                UserProfileCard(navController,it, viewModel)
                BooksPager(mainNavController, it.id)
            } ?: Text("Loading...")

        } else {
            Text(text = "Try Again!!")
        }

    }
}


@Composable
fun UserProfileCard(navController: NavController, user: User, viewModel: UserViewModel) {

    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    val showDialogForAddAddress = remember { mutableStateOf(false) }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri.value = it
                val file = uriToFile(context, it)
                viewModel.uploadUserImage(user.id, file)
            }
        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                val uri = saveBitmapToCache(context, it)
                imageUri.value = uri
                val file = uriToFile(context, uri)
                viewModel.uploadUserImage(user.id, file)
            }
        }

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

            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = user.avatar,
                    contentDescription = "Book Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder)
                )
                IconButton(
                    onClick = { showDialog.value = true },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color.Gray, CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Photo", tint = Color.Black)
                }
            }


            // Dialog to pick an option
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Select Image") },
                    text = {
                        Column {
                            TextButton(onClick = {
                                showDialog.value = false
                                pickImageLauncher.launch("image/*")
                            }) {
                                Text("Pick from Gallery")
                            }
                            TextButton(onClick = {
                                showDialog.value = false
                                takePictureLauncher.launch(null)
                            }) {
                                Text("Take a Picture")
                            }
                        }
                    },
                    confirmButton = {}
                )
            }

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
                onClick = {
                    showDialogForAddAddress.value = true
                },
                content = { Text("Add Address") },
                modifier = Modifier.weight(1f)
            )

            AddAddressDialog(showDialogForAddAddress, viewModel)
            Spacer(modifier = Modifier.width(18.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(HomeTabItem.AddBook.route)
                },
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


fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "profile_pic.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return file.toUri()
}

fun uriToFile(context: Context, uri: Uri): File {
    val file = File(context.cacheDir, "upload_image.jpg")
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return file
}

@Composable
fun AddAddressDialog(showDialog: MutableState<Boolean>, viewModel: UserViewModel) {
    val context = LocalContext.current
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var isLoading by remember { mutableStateOf(false)  }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Add Address") },
            text = {
                Column {
                    OutlinedTextField(
                        value = street,
                        onValueChange = { street = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        label = { Text("Street") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        label = { Text("City") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state,
                        onValueChange = { state = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        label = { Text("State") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = zipCode,
                        onValueChange = { zipCode = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Zip Code") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            isLoading = true
                            getCurrentLocation(context, fusedLocationClient) { location ->
                                if (location != null) {
                                    latitude = location.latitude
                                    longitude = location.longitude
                                }
                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text("Fetch Location", fontSize = 16.sp)
                    }
                    FullScreenLoader(isLoading)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Latitude: $latitude", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Longitude: $longitude", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                }
            },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    val id = context.getIntData(USER.ID.name, 0)
                    val address = Address(
                        user = id,
                        street = street,
                        city = city,
                        state = state,
                        zip_code = zipCode,
                        latitude = latitude,
                        longitude = longitude
                    )
                    viewModel.addAddress(address)
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun FullScreenLoader(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

fun getCurrentLocation(context: Context, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (Location?) -> Unit) {

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.e("Location", "get location: $location")
            onLocationReceived(location)
        }.addOnFailureListener {
            Log.e("Location", "Failed to get location: ${it.message}")
            onLocationReceived(null)
        }
    } else {
        Log.e("Location", "Location permission not granted")
        onLocationReceived(null)
    }
}