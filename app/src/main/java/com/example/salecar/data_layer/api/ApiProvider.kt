package com.example.salecar.data_layer.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {

    fun api() = Retrofit.Builder().baseUrl(BASEURL).client(OkHttpClient.Builder().build())
        .addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(ApiService::class.java)
}