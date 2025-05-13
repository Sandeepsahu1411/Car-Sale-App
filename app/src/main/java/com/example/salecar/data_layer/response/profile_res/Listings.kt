package com.example.salecar.data_layer.response.profile_res

data class Listings(
    val contact_no: String,
    val description: String,
    val email: String,
    val id: Int,
    val images: List<String>,
    val price: String,
    val query_count: Int,
    val title: String
)