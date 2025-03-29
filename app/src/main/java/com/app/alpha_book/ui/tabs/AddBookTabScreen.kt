package com.app.alpha_book.ui.tabs

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.app.alpha_book.model.Address
import com.app.alpha_book.model.BookUploadModel
import com.app.alpha_book.model.Category
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.sharedPreferences.USER
import com.app.alpha_book.remote.sharedPreferences.getIntData
import com.app.alpha_book.ui.viewModel.UserViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun AddBookTabScreen(navController: NavController) {

    val bookTypes = listOf("resell", "new", "pdf", "free_book")
    val conditions = listOf("excellent", "very_good", "good", "average", "poor")
    val readAccessOptions = listOf("paid", "free")

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var bookType by remember { mutableStateOf(bookTypes[0]) }
    var condition by remember { mutableStateOf(conditions[3]) }
    var readAccess by remember { mutableStateOf(readAccessOptions[0]) }
    var selectedImageUris = remember { mutableStateListOf<Uri>() }
    var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
    var selectedAddress by remember { mutableStateOf<Address?>(null) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    var titleError by remember { mutableStateOf(false) }
    var authorError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var imageError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel = remember { UserViewModel(ApiImpl()) }
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val addressList by viewModel.addressListState.collectAsState()

    Column {
        HorizontalPager(
            count = 4, state = pagerState, modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) { page ->
            when (page) {
                0 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "Add Book Information",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
                        )
                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                titleError = it.isBlank()
                            },
                            label = { Text("Book Title") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = titleError
                        )
                        if (titleError) Text("Title is required", color = Color.Red)

                        OutlinedTextField(
                            value = author,
                            onValueChange = {
                                author = it
                                authorError = it.isBlank()
                            },
                            label = { Text("Author Name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = authorError
                        )
                        if (authorError) Text("Author name is required", color = Color.Red)

                        OutlinedTextField(
                            value = description,
                            onValueChange = {
                                description = it
                                descriptionError = it.isBlank()
                            },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            isError = descriptionError
                        )
                        if (descriptionError) Text("Description is required", color = Color.Red)

                        SearchableCategoryField(viewModel) {
                            selectedCategory = it
                            categoryError = false
                        }
                        if (categoryError) Text("Category is required", color = Color.Red)
                    }
                }

                1 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                        text = "Set Book Attributes",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
                    )
                        DropdownMenuField(label = "Book Type",
                            options = bookTypes,
                            selectedOption = bookType,
                            onOptionSelected = { bookType = it })

                        DropdownMenuField(label = "Condition",
                            options = conditions,
                            selectedOption = condition,
                            onOptionSelected = { condition = it })

                        DropdownMenuField(label = "Read Access",
                            options = readAccessOptions,
                            selectedOption = readAccess,
                            onOptionSelected = { readAccess = it })
                    }
                }

                2 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "Pick Book Images",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
                        )
                        if (bookType != "pdf") {
                            ImagePicker(selectedImageUris) { uri ->
                                selectedImageUris.add(uri)
                                imageError = selectedImageUris.isEmpty()
                            }
                        } else {
                            PdfPicker(selectedPdfUri) { uri ->
                                selectedPdfUri = uri
                                imageError = uri == null
                            }
                        }
                        if (imageError) Text("Image or PDF is required", color = Color.Red)
                    }
                }

                3 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "Set Book Price and Location",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
                        )
                        val userId = LocalContext.current.getIntData(USER.ID.name, 0)
                        LaunchedEffect(Unit) { viewModel.getAddress(userId) }

                        OutlinedTextField(
                            value = price,
                            onValueChange = {
                                price = it
                                priceError = it.isBlank()
                            },
                            label = { Text("Price") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = priceError
                        )
                        if (priceError) Text("Price is required", color = Color.Red)


                        Text(
                            text = "Select Address",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                        )

                        addressList?.forEach { address ->
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedAddress = address }
                                .padding(4.dp),
                                elevation = CardDefaults.cardElevation(4.dp)) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = selectedAddress == address,
                                        onClick = { selectedAddress = address })
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${address.street}, ${address.city}, ${address.state}, ${address.zip_code}",
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        } ?: Text(text = "No addresses available", fontSize = 16.sp)
                        if (addressError) Text("Address selection is required", color = Color.Red)
                    }
                }
            }
        }

        // Navigation Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (pagerState.currentPage != 0) {
                if (pagerState.currentPage != pagerState.pageCount - 1) {
                    Button(modifier = Modifier.weight(1f), onClick = {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    }) {
                        Text("Previous")
                    }
                }
            }

            Button(modifier = Modifier.weight(1f), onClick = {
                when (pagerState.currentPage) {
                    0 -> {
                        titleError = title.isBlank()
                        authorError = author.isBlank()
                        descriptionError = description.isBlank()
                        categoryError = selectedCategory == null
                        if (!titleError && !authorError && !descriptionError && !categoryError) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        }
                    }


                    2 -> {
                        imageError =
                            (bookType != "pdf" && selectedImageUris == null) || (bookType == "pdf" && selectedPdfUri == null)
                        if (!imageError) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        }
                    }

                    3 -> {
                        priceError = price.isBlank()
                        addressError = selectedAddress == null
                    }

                    else -> scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                }
            }) {
                if (pagerState.currentPage == (pagerState.pageCount - 1)) {
                    Text("Done", modifier = Modifier.clickable {
                        val book = BookUploadModel(
                            title = title,
                            author = author,
                            description = description,
                            categoryId = selectedCategory!!.id,
                            locationId = selectedAddress!!.id!!,
                            price = price.toDouble(),
                            condition = condition,
                            bookType = bookType,
                            pdfFile = selectedPdfUri?.toString(),
                            readAccess = readAccess,
                            images = getBytesFromUris(context, selectedImageUris),
                            sellerId = context.getIntData(USER.ID.name, 0)
                        )
                        Log.i("TAG", "AddBookTabScreen: $book")
                        viewModel.uploadBook(book)
                    })
                } else Text("Next")
            }
        }
    }
}


@Composable
fun PdfPicker(selectedPdfUri: Uri?, onPdfSelected: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onPdfSelected(it) }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        selectedPdfUri?.let {
            Text(text = "PDF Selected: ${it.lastPathSegment}", textAlign = TextAlign.Center)
        }

        Button(onClick = { launcher.launch("application/pdf") }) {
            Text("Pick PDF")
        }
    }
}

@Composable
fun ImagePicker(selectedImages: SnapshotStateList<Uri>, onImageSelected: (Uri) -> Unit) {
    val context = LocalContext.current

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { onImageSelected(it) }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                onImageSelected(saveBitmapToCache(context, it))
            }
        }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // First Row: Display Selected Images
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(selectedImages) { uri ->
                Box(
                    modifier = Modifier
                        .size(height = 180.dp, width = 120.dp)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(uri),
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )

                    // Cancel Button (Top-Right Corner)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopEnd)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            .clickable {
                                selectedImages.remove(uri)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove Image",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        // Second Row: Camera and Gallery Buttons
        Text(
            text = "Add Images",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Gray)
                    .clickable { cameraLauncher.launch() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Camera, contentDescription = "Camera", tint = Color.Black)
            }

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Gray)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Folder, contentDescription = "Gallery", tint = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        /*     // Third Row: Show Images from DCIM Folder
             LazyRow(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(8.dp)
             ) {
                 items(getDcimImages(context)) { uri ->
                     Image(
                         painter = rememberImagePainter(uri),
                         contentDescription = "DCIM Image",
                         modifier = Modifier
                             .size(80.dp)
                             .clip(RoundedCornerShape(8.dp))
                             .padding(4.dp)
                             .clickable { onImageSelected(uri) }
                     )
                 }
             }*/
    }
}


fun getDcimImages(context: Context): List<Uri> {
    val images = mutableListOf<Uri>()
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
    val selectionArgs = arrayOf("DCIM/%")
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    context.contentResolver.query(queryUri, projection, selection, selectionArgs, sortOrder)
        ?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val imageId = cursor.getLong(columnIndex)
                val imageUri = ContentUris.withAppendedId(queryUri, imageId)
                images.add(imageUri)
            }
        }
    return images
}

@Composable
fun DropdownMenuField(
    label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    Modifier.clickable { expanded = true })
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}


@Composable
fun SearchableCategoryField(viewModel: UserViewModel, onCategorySelected: (Category) -> Unit) {
    var query by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val categoryList by viewModel.categoryList.collectAsState(initial = emptyList())

    LaunchedEffect(query) {
        if (query.isNotEmpty()) viewModel.searchCategories(query)
    }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Category") },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    Modifier.clickable { showDialog = true })
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            readOnly = true
        )

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Select Category", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(value = query, onValueChange = {
                            query = it
                            viewModel.searchCategories(it)
                        }, label = { Text("Search") }, modifier = Modifier.fillMaxWidth()
                        )

                        LazyColumn(
                            modifier = Modifier.height(200.dp)
                        ) {
                            categoryList?.size?.let {
                                items(it) { index ->
                                    Text(text = categoryList!![index].name.toString(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                query = categoryList!![index].name.toString()
                                                onCategorySelected(categoryList!![index])
                                                showDialog = false
                                            }
                                            .padding(12.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getBytesFromUris(context: Context, uris: SnapshotStateList<Uri>): List<ByteArray> {
    return uris.map { uri ->
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        }!!
    }
}

