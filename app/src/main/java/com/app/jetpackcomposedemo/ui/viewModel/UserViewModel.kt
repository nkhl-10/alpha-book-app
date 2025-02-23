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

    private val _bookDetailsState = MutableStateFlow<ApiResponse<Book>?>(null)
    val bookDetailsState: StateFlow<ApiResponse<Book>?> = _bookDetailsState

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


    fun getUserByBookList(userId: Int) {
        viewModelScope.launch {
            try {
                val response = api.getUserByBooks(userId)
                _bookListState.value = response.data
            } catch (e: Exception) {
                _bookListState.value = null
            }
        }
    }

        fun getOrderedByBookList(bookId: Int) {
        viewModelScope.launch {
            try {
                val response = api.getUserByOrderedBooks(bookId)
                _bookListState.value = response.data
            } catch (e: Exception) {
                _bookListState.value = null
            }
        }
    }


    fun getBookList(bookId: Int) {
        viewModelScope.launch {
            try {
                val response = api.getUserByOrderedBooks(bookId)
                _bookListState.value = response.data
            } catch (e: Exception) {
                _bookListState.value = null
            }
        }
    }

    private val _searchResults = MutableStateFlow<List<Book>>(emptyList())
    val searchResults: StateFlow<List<Book>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchBooks(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.searchBooks(query)
                _searchResults.value = response.data ?: emptyList()
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            }
            _isLoading.value = false
        }
    }



}
