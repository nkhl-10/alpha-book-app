package com.app.jetpackcomposedemo.ui.viewModel

import androidx.lifecycle.ViewModel
import com.app.jetpackcomposedemo.model.ApiResponse
import com.app.jetpackcomposedemo.model.LoginRequest
import com.app.jetpackcomposedemo.model.TokenResponse
import com.app.jetpackcomposedemo.model.User
import com.app.jetpackcomposedemo.remote.api.ApiInterface

class UserViewModel(private val api: ApiInterface) : ViewModel() {
    suspend fun createUser(newUser: User): ApiResponse<User> = api.register(newUser)

    suspend fun loginUser(cred: LoginRequest): ApiResponse<TokenResponse> = api.login(cred)
}
