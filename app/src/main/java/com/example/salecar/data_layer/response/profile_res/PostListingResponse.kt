package com.example.salecar.data_layer.response.profile_res

data class PostListingResponse(
    val listings: List<Listings>,
    val username: String
)