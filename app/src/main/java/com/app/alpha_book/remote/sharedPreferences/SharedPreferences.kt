package com.app.alpha_book.remote.sharedPreferences

import android.content.Context
import android.content.SharedPreferences



// Get SharedPreferences instance
fun Context.getPreferences(): SharedPreferences {
    return getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
}

// Save a string value in SharedPreferences
fun Context.saveStringData(key: String, value: String) {
    val sharedPreferences = getPreferences()
    with(sharedPreferences.edit()) {
        putString(key, value)
        apply() // Apply asynchronously
    }
}
fun Context.saveBooleanData(key: String, value: Boolean) {
    val sharedPreferences = getPreferences()
    with(sharedPreferences.edit()) {
        putBoolean(key, value)
        apply() // Apply asynchronously
    }
}

// Retrieve a string value from SharedPreferences
fun Context.getStringData(key: String, defaultValue: String): String {
    val sharedPreferences = getPreferences()
    return sharedPreferences.getString(key, defaultValue) ?: defaultValue
}
fun Context.getBooleanData(key: String, defaultValue: Boolean): Boolean {
    val sharedPreferences = getPreferences()
    return sharedPreferences.getBoolean(key, defaultValue)
}

// Remove a specific key from SharedPreferences
fun Context.removeData(key: String) {
    val sharedPreferences = getPreferences()
    with(sharedPreferences.edit()) {
        remove(key)
        apply() // Apply asynchronously
    }
}

// Clear all data from SharedPreferences
fun Context.clearAllData() {
    val sharedPreferences = getPreferences()
    with(sharedPreferences.edit()) {
        clear() // Clear all data
        apply() // Apply asynchronously
    }
}
