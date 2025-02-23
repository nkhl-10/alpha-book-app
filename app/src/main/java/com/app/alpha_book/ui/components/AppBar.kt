package com.app.alpha_book.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, onNavigationIconClick: () -> Unit = {}) {
    TopAppBar(
        title = {
            Text(
                text = title,
                Modifier.fillMaxWidth().padding(8.dp),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Start
            )
        },
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