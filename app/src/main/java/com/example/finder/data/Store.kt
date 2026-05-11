package com.example.finder.data

data class Store(
    val id: Int,
    val name: String,
    val address: String,
    val distance: String,
    val isOpen: Boolean,
    val contact: String,
    val latitude: Double,
    val longitude: Double
)
