package com.app.jetpackcomposedemo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String = "",
    @SerialName("author") val author: String? = null,
    @SerialName("description") val description: String? = null,

    @SerialName("category") val category: Category? = null,  // Nullable
    @SerialName("location") val location: Location? = null,  // Nullable

    @SerialName("price") val price: String? = null,  // Nullable
    @SerialName("condition") val condition: String? = null,
    @SerialName("book_type") val bookType: String? = null,  // Nullable

    @SerialName("pdf_file") val pdfFile: String? = null,  // Nullable
    @SerialName("read_access") val readAccess: String? = null,  // Nullable

    @SerialName("created_at") val createdAt: String? = null,  // Nullable
    @SerialName("updated_at") val updatedAt: String? = null,  // Nullable
    @SerialName("images") val images: List<BookImage>? = emptyList(),  // Nullable list

    @SerialName("seller") val sellerId: Int? = null  // Nullable
)

@Serializable
data class Category(
    @SerialName("id") val id: Int? = 0,  // Nullable
    @SerialName("name") val name: String? = "",
    @SerialName("description") val description: String? = ""
)

@Serializable
data class Location(
    @SerialName("id") val id: Int? = null,
    @SerialName("user") val user: Int? = null,
    @SerialName("street") val street: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("state") val state: String? = null,
    @SerialName("zip_code") val zipCode: String? = null,
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null
)

@Serializable
data class BookImage(
    @SerialName("id") val id: Int? = null,
    @SerialName("image_url") val imageUrl: String? = null  // Map 'image_url' from JSON to 'imageUrl' in the model
)
