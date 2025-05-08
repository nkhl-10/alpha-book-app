package com.app.alpha_book.ui.screen

//noinspection UsingMaterialAndMaterial3Libraries
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.alpha_book.model.Transaction
import com.app.alpha_book.model.TransactionConfirm
import com.app.alpha_book.remote.api.ApiImpl
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.ui.navigation.Argument
import com.app.alpha_book.ui.tabs.BookListRow
import com.app.alpha_book.ui.utils.ApiStatus
import com.app.alpha_book.ui.utils.CenterLoadingView
import com.app.alpha_book.ui.viewModel.UserViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isSeller = navBackStackEntry?.arguments?.getBoolean(Argument.IS_SELLER_PAGE.name)
    val transactionId = navBackStackEntry?.arguments?.getInt(Argument.TRANSACTION_ID.name)
    val context = LocalContext.current

    if (isSeller == null || transactionId == null) {
        Text(text = "Something went wrong", color = Color.Red)
        return
    }

    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
    val transactionList by viewModel.transactionList.collectAsState()

    LaunchedEffect(transactionId) {
        viewModel.getTransaction(transactionId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Transaction Details") },
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
            transactionList?.firstOrNull()?.let { transaction ->
                var otp by remember { mutableStateOf("") }
                var isLoading by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BookListRow(listOf(transaction), isSeller, navController)

                    // Seller View
                    if (isSeller) {
                        SellerLocationView(transaction, context)
                        SellerPhoneCall(transaction, context)
                        OTPSection(transaction.otp)
                    }
                    // Buyer View
                    else {

                        BuyerOTPInput { enteredOtp -> otp = enteredOtp }

                        SwipeToConfirmButton {
                            if (otp.length != 6) {
                                Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                                return@SwipeToConfirmButton
                            }
                            MainScope().launch {
                                isLoading = true
                                val data = TransactionConfirm(transactionId, otp.toIntOrNull())
                                val message = viewModel.transaction(data)
                                if (message.status ==ApiStatus.SUCCESS.code){
                                    Toast.makeText(context, "Order are Complete", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                                isLoading = false
                            }
                        }
                        if (isLoading) CenterLoadingView()
                    }
                }
            } ?: CenterLoadingView()
        }
    )
}

@Composable
fun SellerLocationView(
    transaction: Transaction,
    context: Context
) {
    transaction.book.location?.let { location ->
        location.latitude?.let { latitude ->
            location.longitude?.let { longitude ->
                GoogleMapView(LatLng(latitude, longitude))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            openGoogleMapsAtLocation(context, latitude, longitude)
                        }
                        .padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Call",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Start Map To Collect Book",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }
    }
}

@Composable
fun SellerPhoneCall(transaction: Transaction, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${transaction.buyer.phone}")
                }
                context.startActivity(intent)
            }
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Call",
                tint = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Call Seller",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Composable
fun OTPSection(otp: Int?) {
    Text("OTP")
    OtpDisplay(otp.toString())
}

@Composable
fun BuyerOTPInput(otp: (String) -> Unit) {
    Text(
        text = "Enter OTP", style = MaterialTheme.typography.titleMedium, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
    OtpInput(onOtpComplete = { otpValue -> otp(otpValue) })
}

fun openGoogleMapsAtLocation(context: Context, fromLat: Double, fromLng: Double) {
    val uri = Uri.parse("geo:$fromLat,$fromLng?q=$fromLat,$fromLng")
    val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}

@Composable
fun OtpDisplay(otp: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        otp.padEnd(6, '*').forEach { char ->  // ensures 6 boxes even if otp is short
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun OtpInput(
    otpLength: Int = 6,
    onOtpComplete: (String) -> Unit
) {
    var otpValue by remember { mutableStateOf("") }

    // Focus manager for moving between boxes
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(otpLength) { index ->
            val char = otpValue.getOrNull(index)?.toString() ?: ""
            OutlinedTextField(
                value = char,
                onValueChange = {
                    if (it.length <= 1 && it.all { ch -> ch.isDigit() }) {
                        val newOtp = StringBuilder(otpValue)
                        if (char.isNotEmpty()) newOtp.setCharAt(index, it[0])
                        else newOtp.insert(index, it)

                        // Update OTP value
                        otpValue = newOtp.toString().take(otpLength)

                        // Move focus to next box
                        if (it.isNotEmpty() && index < otpLength - 1) {
                            focusManager.moveFocus(FocusDirection.Next)
                        }

                        // Trigger callback when complete
                        if (otpValue.length == otpLength) {
                            onOtpComplete(otpValue)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .width(48.dp)
                    .height(60.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}

@Composable
fun SwipeToConfirmButton(
    modifier: Modifier = Modifier,
    text: String = "Swipe to Complete Order",
    onSwipeComplete: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx: Float = with(density) {
        configuration.screenWidthDp.dp.toPx()
    }

    val swipeOffset = remember { Animatable(0f) }
    val maxSwipeDistance = screenWidthPx - 210 // Adjust width as needed
    val isSwiped = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(50))
            .background(
                if (isSwiped.value) MaterialTheme.colorScheme.primary
                else Color.LightGray.copy(alpha = 0.4f)
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    val newOffset = (swipeOffset.value + dragAmount).coerceIn(0f, maxSwipeDistance)
                    scope.launch {
                        swipeOffset.snapTo(newOffset)
                    }

                    if (newOffset >= maxSwipeDistance && !isSwiped.value) {
                        isSwiped.value = true
                        onSwipeComplete()
                    }
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = if (isSwiped.value) Color.White else Color.DarkGray
        )

        if (!isSwiped.value) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(swipeOffset.value.roundToInt(), 0) }
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Swipe",
                    tint = Color.White
                )
            }
        }

    }
}
