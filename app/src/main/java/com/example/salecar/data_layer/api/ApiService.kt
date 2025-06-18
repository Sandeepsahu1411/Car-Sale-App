package com.example.salecar.data_layer.api

import com.example.salecar.data_layer.response.car_delete_res.CarPostDeteleResponse
import com.example.salecar.data_layer.response.car_detail_res.CarDetailResponse
import com.example.salecar.data_layer.response.car_edit_res.CarEditResponse
import com.example.salecar.data_layer.response.car_post_res.CarPostResponse
import com.example.salecar.data_layer.response.category_res.CarCategoryResponse
import com.example.salecar.data_layer.response.home_res.HomeScreenResponse
import com.example.salecar.data_layer.response.login_res.LoginResponse
import com.example.salecar.data_layer.response.profile_res.PostListingResponse
import com.example.salecar.data_layer.response.signup_res.SignUpResponse
import com.example.salecar.data_layer.response.user_res.GetUserByIdResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
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


    @Multipart
    @POST("submitCar.php")
    suspend fun carPost(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<CarPostResponse>

    @GET("getCategories.php")
    suspend fun getCarCategory(): Response<CarCategoryResponse>

    @GET("userlisting.php")
    suspend fun getUserById(
        @Query("id") id: String
    ): Response<GetUserByIdResponse>

    @GET("user_listing_dashboard.php")
    suspend fun getPostListing(
        @Query("user_id") id: String
    ): Response<PostListingResponse>

    @FormUrlEncoded
    @POST("delete_car_post.php")
    suspend fun deleteCarPost(
        @Field("id") id: String
    ): Response<CarPostDeteleResponse>


    @Multipart
    @POST("edit_car_post.php")
    suspend fun editCarPost(
        @Query("id") id: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<CarEditResponse>


}
