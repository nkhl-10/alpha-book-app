package com.app.jetpackcomposedemo.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.jetpackcomposedemo.model.ApiResponse
import com.app.jetpackcomposedemo.model.Book
import com.app.jetpackcomposedemo.model.LoginRequest
import com.app.jetpackcomposedemo.model.TokenResponse
import com.app.jetpackcomposedemo.model.User
import com.app.jetpackcomposedemo.remote.api.ApiInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val api: ApiInterface) : ViewModel() {


    private val _bookListState = MutableStateFlow<List<Book>?>(null)
    val bookList: StateFlow<List<Book>?> = _bookListState

    private val _bookDetailsState = MutableStateFlow<List<Book>?>(null)
    val bookDetailsState: StateFlow<List<Book>?> = _bookDetailsState

    suspend fun createUser(newUser: User): ApiResponse<User> = api.register(newUser)

    suspend fun loginUser(cred: LoginRequest): ApiResponse<TokenResponse> = api.login(cred)


    fun getBookList() {
        viewModelScope.launch {
            try {
                val response = api.getBooks()
                _bookListState.value = response.data
                Log.i("DATAAPI", "Book List: $response")
            } catch (e: Exception) {
                Log.e("DATAAPI", "Error fetching books: ${e.message}")
                _bookListState.value = emptyList()
            }
        }
    }

    fun getBookList(bookId: Int) {
        viewModelScope.launch {
            try {
                val response = api.getBooks(bookId)
                _bookDetailsState.value = response.data
            } catch (e: Exception) {
                _bookDetailsState.value = null
            }
        }
    }

}
