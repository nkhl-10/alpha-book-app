package com.app.jetpackcomposedemo.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, onNavigationIconClick: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = title) },
       /* navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = onNavigationIconClick ) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
            }
        }*/
    )
}