package com.app.jetpackcomposedemo.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.jetpackcomposedemo.remote.api.ApiImpl
import com.app.jetpackcomposedemo.remote.api.ApiInterface
import com.app.jetpackcomposedemo.remote.sharedPreferences.USER
import com.app.jetpackcomposedemo.remote.sharedPreferences.getStringData
import com.app.jetpackcomposedemo.ui.viewModel.UserViewModel

@Composable
fun HomeTabScreen() {
    val context = LocalContext.current

    // Create ViewModel and API instances
    val userApi: ApiInterface = ApiImpl()
    val viewModel = remember { UserViewModel(userApi) }
/*

    // Fetch user data once using LaunchedEffect
    LaunchedEffect(Unit) {
        viewModel.getTodos(context.getStringData(USER.USER_ID.name, "1"))
    }

    val todoList =
    // UI Composition
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (viewModel.todoListResponse.isEmpty()){
                    Text(text = "Loading...")
                }else{
                    TodosList(list = viewModel.todoListResponse)
                }
    }
}

@Composable
fun TodosList(list:List<Todo>) {
    LazyColumn{
        itemsIndexed(items = list){index,item ->
                TodoItem(todo = item)
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    // Display each Todo item here
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(
            text = todo.title,  // Use the 'title' from the Todo object
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 18.sp)
        )
        Text(
            text = todo.description ?: "",  // Use the 'description' from the Todo object
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 15.sp)
        )
        Text(
            text = todo.createdAt ?: "",  // Use the 'description' from the Todo object
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 12.sp)
        )
    }*/
}
