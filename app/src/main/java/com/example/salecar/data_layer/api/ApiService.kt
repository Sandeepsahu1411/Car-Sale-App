package com.example.salecar.data_layer.api

import com.example.salecar.data_layer.response.car_detail_res.CarDetailResponse
import com.example.salecar.data_layer.response.home_res.HomeScreenResponse
import com.example.salecar.data_layer.response.login_res.LoginResponse
import com.example.salecar.data_layer.response.signup_res.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register.php")
    suspend fun signup(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SignUpResponse>

    @GET("home.php")
    suspend fun getHomeScreen(): Response<HomeScreenResponse>

    @GET("get_car_details.php")
    suspend fun carDetail(
        @Query("id") id: String
    ): Response<CarDetailResponse>



}
