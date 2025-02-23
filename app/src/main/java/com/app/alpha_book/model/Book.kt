package com.app.alpha_book.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    @SerialName("id") val id: Int = 0,
    @SerialName("title") val title: String = "",
    @SerialName("author") val author: String? = null,
    @SerialName("description") val description: String? = null,

    @SerialName("category") val category: Category? = null,
    @SerialName("location") val location: Location? = null,

    @SerialName("price") val price: String? = null,
    @SerialName("condition") val condition: String? = null,
    @SerialName("book_type") val bookType: String? = null,

    @SerialName("pdf_file") val pdfFile: String? = null,
    @SerialName("read_access") val readAccess: String? = null,

    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("images") val images: List<BookImage>? = emptyList(),
    @SerialName("seller") val sellerId: Int? = null
)

@Serializable
data class Category(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null
)

@Serializable
data class Location(
    @SerialName("id") val id: Int = 0,
    @SerialName("user") val user: Int = 0,
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
    @SerialName("image_url") val imageUrl: String? = null
)
