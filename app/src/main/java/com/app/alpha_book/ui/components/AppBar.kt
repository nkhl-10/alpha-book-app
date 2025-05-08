package com.app.alpha_book.ui.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.app.alpha_book.base.MainActivity
import com.app.alpha_book.remote.sharedPreferences.clearAllData
import com.app.alpha_book.ui.viewModel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, viewModel: UserViewModel) {
    val context = LocalContext.current

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) getCurrentLocation(context, fusedLocationClient) { location ->
            Log.d("Location", "User Location: $location")
        }
        else Log.e("Permission", "Permission denied")
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Start
            )
        },
        actions = {
            if (title == "Profile") {
                IconButton(onClick = {
                    context.clearAllData()
                    context.startActivity(Intent(context, MainActivity::class.java))
                }) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "More")
                }
            }
            else if (title == "Home"){
               viewModel.locationName?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = viewModel.locationName!!)
                    }
                }?: IconButton(onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        getCurrentLocation(context, fusedLocationClient) { location ->
                            if (location != null) {
                                val geocoder = Geocoder(context, Locale.getDefault())
                                try {
                                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                   val addressName= if (!addresses.isNullOrEmpty()) addresses[0].locality
                                     else "Unknown Location"
                                    viewModel.updateLocation(LatLng(location.latitude,location.longitude),addressName)
                                } catch (e: IOException) {
                                    viewModel.updateLocation(LatLng(location.latitude,location.longitude),"Location Error")
                                }
                            }
                        }
                    else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Get Location")
                }
            }
        }
    )
}


fun getCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Location?) -> Unit
) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    if (!isEnabled) {
        Toast.makeText(context, "Please enable location services", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        onLocationReceived(null)
        return
    }

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