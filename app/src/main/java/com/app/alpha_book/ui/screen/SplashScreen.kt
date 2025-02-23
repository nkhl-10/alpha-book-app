package com.app.alpha_book.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.alpha_book.remote.sharedPreferences.USER
import com.app.alpha_book.remote.sharedPreferences.getBooleanData
import com.app.alpha_book.ui.navigation.ScreenNavigationItem
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current

    // Introduce a delay using LaunchedEffect
    LaunchedEffect(Unit) {
        // Simulate a 2-second delay
        delay(2000)
        // Navigate to the Login Screen


        val isLogged = context.getBooleanData(USER.UserIsLogged.name,false)

        if (isLogged){
            navController.navigate(ScreenNavigationItem.Home.route) {
                popUpTo(ScreenNavigationItem.Splash.route) { inclusive = true }
            }
        }else{
            navController.navigate(ScreenNavigationItem.Login.route){
                popUpTo(ScreenNavigationItem.Splash.route){inclusive=true}
            }
        }

    }

    // Design for the Splash Screen
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Text(
            text = "Welcome to the App",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
    }
}
