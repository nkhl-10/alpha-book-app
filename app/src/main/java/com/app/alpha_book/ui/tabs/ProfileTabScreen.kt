package com.app.alpha_book.ui.tabs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import com.app.alpha_book.ui.utils.ApiStatus
import com.app.alpha_book.ui.utils.CenterLoadingView
import com.app.alpha_book.ui.utils.NoBookAvailable
import com.app.alpha_book.ui.viewModel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileOutputStream

@Composable
fun ProfileTabScreen(mainNavController: NavController) {
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
                UserProfileCard(it, viewModel)
                BooksPager(mainNavController, it.id)
            } ?: CenterLoadingView()
        } else CenterLoadingView()

    }
}


@Composable
fun UserProfileCard(user: User, viewModel: UserViewModel) {

    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    val showDialogForAddAddress = remember { mutableStateOf(false) }
    val showDialogForManageAddAddress = remember { mutableStateOf(false) }
    val showDialogEditProfile = remember { mutableStateOf(false) }
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
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
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
                Text(text = user.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(6.dp))

                Text(text = user.username)
                Spacer(modifier = Modifier.height(6.dp))

                Text(text = user.email)
                Spacer(modifier = Modifier.height(6.dp))

                Text(text = user.phone ?: "Not Available Phone No")
            }
        }

        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { showDialogForAddAddress.value = true },
                content = { Text("Add Address", style = MaterialTheme.typography.bodySmall) },
            )

            AddAddressDialog(showDialogForAddAddress, viewModel)
            Spacer(modifier = Modifier.width(5.dp))

            OutlinedButton(
                onClick = { showDialogForManageAddAddress.value = true },
                content = { Text("Manage Address", style = MaterialTheme.typography.bodySmall) },
            )

            ManageAddressDialog(showDialogForManageAddAddress, viewModel)
            Spacer(modifier = Modifier.width(5.dp))

            OutlinedButton(
                onClick = { showDialogEditProfile.value = true },
                content = { Text("Edit Profile", style = MaterialTheme.typography.bodySmall) },
            )

            EditProfileDialog(showDialogEditProfile, viewModel)
        }
    }
}

@Composable
fun EditProfileDialog(showDialogEditProfile: MutableState<Boolean>, viewModel: UserViewModel) {

    val context = LocalContext.current
    val user by viewModel.userState.collectAsState()

    val name = remember { mutableStateOf(user?.data?.name ?: "") }
    val email = remember { mutableStateOf(user?.data?.email ?: "") }
    val phone = remember { mutableStateOf(user?.data?.phone ?: "") }


    LaunchedEffect(Unit) {
        val id = context.getIntData(USER.ID.name, 0)
        viewModel.getUser(id)
        user!!.let {
            name.value = it.data?.name ?: ""
            email.value = it.data?.email ?: ""
            phone.value = it.data?.phone ?: ""
        }
    }

    if (showDialogEditProfile.value) {
        AlertDialog(
            onDismissRequest = { showDialogEditProfile.value = false },
            title = { Text("Edit Profile") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text("Name") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Email") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phone.value,
                        onValueChange = { phone.value = it },
                        label = { Text("Phone") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val updatedUser = user?.data?.copy(
                        name = name.value,
                        email = email.value,
                        phone = phone.value
                    )
                    Log.i("TAG", "EditProfileDialog: $updatedUser")
                    if (updatedUser != null) {
                        viewModel.updateUser(user!!.data!!.id, updatedUser)
                    }
                    showDialogEditProfile.value = false
                }) {
                    Text("Update")
                }
            }
        )


    }
}

@Composable
fun BooksPager(navController: NavController, id: Int) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val tabTitles = listOf("Your Books", "Ordered Books", "Sold Book")
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
                            bookList == null -> CenterLoadingView()
                            bookList!!.isEmpty() -> NoBookAvailable()
                            else -> BookList(list = bookList.orEmpty()) { bookId ->
                                navController.navigate(ScreenNavigationItem.BookDetails.route + "/${bookId}/" + true)
                            }
                        }
                    }
                }

                1 -> {
                    val bookList by viewModel.transactionList.collectAsState()
                    LaunchedEffect(Unit) { viewModel.getOrderedByBookList(id) }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        when {
                            bookList == null -> CenterLoadingView()
                            bookList!!.isEmpty() -> NoBookAvailable()
                            else -> BookListRow(list = bookList.orEmpty(), true, navController)
                        }
                    }
                }

                2 -> {
                    val bookList by viewModel.transactionList.collectAsState()
                    LaunchedEffect(Unit) { viewModel.soldByUserBookList(id) }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        when {
                            bookList == null -> CenterLoadingView()
                            bookList!!.isEmpty() -> NoBookAvailable()
                            else -> BookListRow(list = bookList.orEmpty(), false, navController)
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
fun ManageAddressDialog(showDialog: MutableState<Boolean>, viewModel: UserViewModel) {
    val context = LocalContext.current
    val addressList by viewModel.addressListState.collectAsState()
    val selectedAddress = remember { mutableStateOf<Address?>(null) }
    val showEditDialog = remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        val id = context.getIntData(USER.ID.name, 0)
        viewModel.getAddress(id)
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Manage Address") },
            text = {
                Column {
                    if (addressList?.isEmpty() == true) Text("No addresses available.")
                    else addressList?.forEach { address ->
                        val isSelected = selectedAddress.value?.id == address.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { selectedAddress.value = address },
                            colors = CardDefaults.cardColors(
                                containerColor =
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Street: ${address.street}")
                                Text(text = "City: ${address.city}")
                                Text(text = "State: ${address.state}")
                                Text(text = "ZIP: ${address.zip_code}")
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    TextButton(
                        onClick = {
                            selectedAddress.value?.let {
                                showEditDialog.value = true
                            }
                        },
                        enabled = selectedAddress.value != null
                    ) { Text("Edit") }

                    EditAddressDialog(
                        showDialog = showEditDialog,
                        addressToEdit = selectedAddress.value,
                        addressId = selectedAddress.value?.id,
                        viewModel = viewModel
                    )

                    TextButton(
                        onClick = {
                            selectedAddress.value?.let {
                                viewModel.deleteAddress(it.id!!.toInt())
                                val id = context.getIntData(USER.ID.name, 0)
                                viewModel.getAddress(id)
                                selectedAddress.value = null
                                showDialog.value = false
                            }
                        },
                        enabled = selectedAddress.value != null,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) { Text("Delete") }
                }
            }
        )
    }
}

@Composable
fun EditAddressDialog(
    showDialog: MutableState<Boolean>,
    addressToEdit: Address?,
    viewModel: UserViewModel,
    addressId: Int?
) {
    if (addressToEdit == null) return

    val context = LocalContext.current
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }

    // Prefill values whenever a new address is set
    LaunchedEffect(addressToEdit) {
        street = addressToEdit.street ?: ""
        city = addressToEdit.city
        state = addressToEdit.state
        zipCode = addressToEdit.zip_code ?: ""
        latitude = addressToEdit.latitude ?: 0.0
        longitude = addressToEdit.longitude ?: 0.0
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Edit Address") },
            text = {
                Column {
                    OutlinedTextField(
                        value = street,
                        onValueChange = { street = it },
                        label = { Text("Street") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state,
                        onValueChange = { state = it },
                        label = { Text("State") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = zipCode,
                        onValueChange = { zipCode = it },
                        label = { Text("Zip Code") })
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Latitude: $latitude", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Longitude: $longitude", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    val updated = addressToEdit.copy(
                        street = street,
                        city = city,
                        state = state,
                        zip_code = zipCode,
                        latitude = latitude,
                        longitude = longitude
                    )
                    if (addressId != null) {
                        viewModel.editAddress(updated, addressId)
                        val userId = context.getIntData(USER.ID.name, 0)
                        viewModel.getAddress(userId)
                    } else Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()

                }) { Text("Update") }
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
fun AddAddressDialog(showDialog: MutableState<Boolean>, viewModel: UserViewModel) {
    val context = LocalContext.current
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var isLoading by remember { mutableStateOf(false) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) getCurrentLocation(context, fusedLocationClient) { location ->
            Log.d("Location", "User Location: $location")
        }
        else Log.e("Permission", "Permission denied")
    }

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
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) getCurrentLocation(context, fusedLocationClient) { location ->
                                if (location != null) {
                                    latitude = location.latitude
                                    longitude = location.longitude
                                }
                                isLoading = false
                            }
                            else {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                showDialog.value = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text("Fetch Location", fontSize = 16.sp)
                    }
                    if (isLoading) CenterLoadingView()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Latitude: $latitude",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Longitude: $longitude",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                }
            },
            confirmButton = {
                Button(onClick = {
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
                    showDialog.value = false
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


fun getCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Location?) -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
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