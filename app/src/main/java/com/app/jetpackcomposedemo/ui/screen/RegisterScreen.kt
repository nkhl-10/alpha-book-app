package com.app.jetpackcomposedemo.ui.screen

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.jetpackcomposedemo.model.ApiResponse
import com.app.jetpackcomposedemo.model.User
import com.app.jetpackcomposedemo.remote.api.ApiImpl
import com.app.jetpackcomposedemo.remote.api.ApiInterface
import com.app.jetpackcomposedemo.ui.navigation.NavigationItem
import com.app.jetpackcomposedemo.ui.utils.ApiStatus
import com.app.jetpackcomposedemo.ui.viewModel.UserViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userApi: ApiInterface = ApiImpl()
    val viewModel = UserViewModel(userApi)


// State variables for the inputs
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    var paasword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

// UI Layout for Login Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null && name.isEmpty(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null && email.isEmpty(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )


            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phoneNo,
                onValueChange = { phoneNo = it },
                label = { Text("Phone No") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null && phoneNo.isEmpty(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(16.dp))


            // Password input field
            OutlinedTextField(
                value = paasword,
                onValueChange = { paasword = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null && paasword.isEmpty(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility  else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Login", modifier = Modifier.clickable {
                    navController.navigate(NavigationItem.Login.route)
                })
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Show error message if present
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Login button
            Button(
                onClick = {
                    errorMessage = null
                    if (name.isEmpty() || paasword.isEmpty() || phoneNo.isEmpty() || email.isEmpty()) {
                        errorMessage = "Please enter details"
                    } else {
                        isLoading = true
                        // Simulate login process
                        MainScope().launch {
                            delay(2000) // Simulate network delay
                            isLoading = false
                            val user = User(
                                email = email,
                                password = paasword,
                                username = name,
                                phone = phoneNo,
                                avatar = "null"
                            )
                            val response = viewModel.createUser(user)
                            Log.i("TAG", "RegisterScreenAuth: $response")
                            if (response.status == ApiStatus.CREATED.code){
                                navController.navigate(NavigationItem.Home.route) {
                                    popUpTo(NavigationItem.Login.route) { inclusive = true }
                                }
                            }else{
                                isLoading = false
                                errorMessage = response.message
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login", style = TextStyle(fontSize = 18.sp))
            }

            // Loading Indicator
            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}
