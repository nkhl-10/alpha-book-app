package com.app.alpha_book.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.alpha_book.model.Address
import com.app.alpha_book.model.ApiResponse
import com.app.alpha_book.model.Book
import com.app.alpha_book.model.BuyReqModel
import com.app.alpha_book.model.Category
import com.app.alpha_book.model.LoginRequest
import com.app.alpha_book.model.TokenResponse
import com.app.alpha_book.model.User
import com.app.alpha_book.remote.api.ApiInterface
import com.app.alpha_book.ui.utils.ApiStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class UserViewModel(private val api: ApiInterface) : ViewModel() {


    private val _bookListState = MutableStateFlow<List<Book>?>(null)
    val bookList: StateFlow<List<Book>?> = _bookListState

    private val _categoryListState = MutableStateFlow<List<Category>?>(null)
    val categoryList: StateFlow<List<Category>?> = _categoryListState

    private val _bookDetailsState = MutableStateFlow<ApiResponse<Book>?>(null)
    val bookDetailsState: StateFlow<ApiResponse<Book>?> = _bookDetailsState

    private val _userState = MutableStateFlow<ApiResponse<User>?>(null)
    val userState: StateFlow<ApiResponse<User>?> = _userState

    private val _uploadState = MutableStateFlow<ApiResponse<String>?>(null)
    val uploadState = _uploadState.asStateFlow()

    suspend fun createUser(newUser: User): ApiResponse<User> = api.register(newUser)

    suspend fun loginUser(cred: LoginRequest): ApiResponse<TokenResponse> = api.login(cred)

    suspend fun buyBook(buyReqModel: BuyReqModel): ApiResponse<String> = api.buy(buyReqModel)

    fun uploadUserImage(userId: Int, file: File) {
        viewModelScope.launch {
            val response = api.uploadUserImage(userId, file)
            if (response.status == ApiStatus.SUCCESS.code) {
                getUser(userId)
            }
            _uploadState.value = response
        }
    }

    fun getBookList() {
        viewModelScope.launch {
            try {
                val response = api.getBook()
                _bookListState.value = response.data
                Log.i("DATAAPI", "Book List: $response")
            } catch (e: Exception) {
                Log.e("DATAAPI", "Error fetching books: ${e.message}")
                _bookListState.value = emptyList()
            }
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            try {
                val response = api.addAddress(address)
                Log.i("DATAAPI", "Address Added: $response")
            } catch (e: Exception) {
                Log.e("DATAAPI", "Error fetching books: ${e.message}")
            }
        }
    }

    fun getCategoryList() {
        viewModelScope.launch {
            try {
                val response = api.getCategories()
                _categoryListState.value = response.data
            } catch (e: Exception) {
                _categoryListState.value = emptyList()
            }
        }
    }

    fun getCategoryList(categoryId: Int) {
        viewModelScope.launch {
            try {
                val response = api.getCategories(categoryId)
                _bookListState.value = response.data
            } catch (e: Exception) {
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
                val response = api.getBook(bookId)
                _bookDetailsState.value = response
            } catch (e: Exception) {
                _bookListState.value = null
            }
        }
    }

    fun getUser(userId: Int) {
        viewModelScope.launch {
            try {
                val response = api.getUser(userId)
                _userState.value = response
            } catch (e: Exception) {
                _userState.value = null
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
