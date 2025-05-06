package com.example.salecar.data_layer.api

import com.example.salecar.data_layer.response.car_detail_res.CarDetailResponse
import com.example.salecar.data_layer.response.car_post_res.CarPostResponse
import com.example.salecar.data_layer.response.home_res.HomeScreenResponse
import com.example.salecar.data_layer.response.login_res.LoginResponse
import com.example.salecar.data_layer.response.signup_res.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
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

//    @FormUrlEncoded
//    @POST("submitCar.php")
//    suspend fun carPost(
//        @Field("category_id") category_id: String,
//        @Field("title") title: String,
//        @Field("description") description: String,
//        @Field("plate_no") plate_no: String,
//        @Field("price") price: String,
//        @Field("email") email: String,
//        @Field("visibility") visibility: String,
//        @Field("address") address: String,
//        @Field("video_link") video_link: String,
//        @Field("website_link") website_link: String,
//        @Field("country") country: String,
//        @Field("latitude") latitude: String,
//        @Field("longitude") longitude: String,
//        @Field("year") year: String,
//        @Field("body_type") body_type: String,
//        @Field("transmission") transmission: String,
//        @Field("colour") colour: String,
//        @Field("seats") seats: String,
//        @Field("doors") doors: String,
//        @Field("luggage_capacity") luggage_capacity: String,
//        @Field("fuel_type") fuel_type: String,
//        @Field("engine_power") engine_power: String,
//        @Field("engine_size") engine_size: String,
//        @Field("top_speed") top_speed: String,
//        @Field("acceleration") acceleration: String,
//        @Field("fuel_consumption") fuel_consumption: String,
//        @Field("fuel_capacity") fuel_capacity: String,
//        @Field("insurance_group") insurance_group: String,
//        @Field("images[]") images: List<String>,
//        @Field("contact_no") contact_no: String,
//        @Field("brochure_engine_size") brochure_engine_size: String,
//
//        ): Response<CarPostResponse>
//
//    @FormUrlEncoded
//    @POST("submitCar.php")
//    suspend fun carPost(
//        @FieldMap fields: Map<String, @JvmSuppressWildcards Any>
//    ): Response<CarPostResponse>

    @Multipart
    @POST("submitCar.php")
    suspend fun carPost(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<CarPostResponse>

}
