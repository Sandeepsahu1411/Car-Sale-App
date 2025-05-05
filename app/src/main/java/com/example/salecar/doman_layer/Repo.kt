package com.example.salecar.doman_layer

import android.util.Log
import android.util.Log.e
import com.example.salecar.ResultState
import com.example.salecar.data_layer.api.ApiProvider
import com.example.salecar.data_layer.response.car_detail_res.CarDetailResponse
import com.example.salecar.data_layer.response.car_post_res.CarPostRequest
import com.example.salecar.data_layer.response.car_post_res.CarPostResponse
import com.example.salecar.data_layer.response.car_post_res.toFieldMap
import com.example.salecar.data_layer.response.home_res.HomeScreenResponse
import com.example.salecar.data_layer.response.login_res.LoginResponse
import com.example.salecar.data_layer.response.signup_res.SignUpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class Repo {
    suspend fun loginRepo(
        email: String,
        password: String
    ): Flow<ResultState<Response<LoginResponse>>> =
        flow {
            emit(ResultState.Loading)
            try {
                val response = ApiProvider.api().login(email, password)
                Log.d("LoginResponse", "Login Response: $response")
                if (response.isSuccessful && response.body() != null) {
                    emit(ResultState.Success(response))

                } else {
                    emit(ResultState.Error("Invalid credentials"))
                }

            } catch (e: Exception) {
                emit(ResultState.Error(e.message.toString()))
            }
        }

    suspend fun signUpRepo(
        username: String,
        email: String,
        password: String
    ): Flow<ResultState<Response<SignUpResponse>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = ApiProvider.api().signup(username, email, password)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))

        }

    }

    suspend fun getHomeScreen(): Flow<ResultState<Response<HomeScreenResponse>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = ApiProvider.api().getHomeScreen()
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }

    }

    suspend fun carDetailRepo(id: String): Flow<ResultState<Response<CarDetailResponse>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = ApiProvider.api().carDetail(id)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    //    suspend fun carPostRepo(
//        category_id: String,
//        title: String,
//        description: String,
//        plate_no: String,
//        price: String,
//        email: String,
//        visibility: String,
//        address: String,
//        video_link: String,
//        website_link: String,
//        country: String,
//        latitude: String,
//        longitude: String,
//        year: String,
//        body_type: String,
//        transmission: String,
//        colour: String,
//        seats: String,
//        doors: String,
//        luggage_capacity: String,
//        fuel_type: String,
//        engine_power: String,
//        engine_size: String,
//        top_speed: String,
//        acceleration: String,
//        fuel_consumption: String,
//        fuel_capacity: String,
//        insurance_group: String,
//        images: List<String>,
//        contact_no: String,
//        brochure_engine_size: String,
//    ): Flow<ResultState<Response<CarPostResponse>>> = flow {
//        emit(ResultState.Loading)
//        try {
//            val response = ApiProvider.api().carPost(
//                category_id,
//                title,
//                description,
//                plate_no,
//                price,
//                email,
//                visibility,
//                address,
//                video_link,
//                website_link,
//                country,
//                latitude,
//                longitude,
//                year,
//                body_type,
//                transmission,
//                colour,
//                seats,
//                doors,
//                luggage_capacity,
//                fuel_type,
//                engine_power,
//                engine_size,
//                top_speed,
//                acceleration,
//                fuel_consumption,
//                fuel_capacity,
//                insurance_group,
//                images,
//                contact_no,
//                brochure_engine_size
//            )
//            emit(ResultState.Success(response))
//        } catch (e: Exception) {
//            emit(ResultState.Error(e.message.toString()))
//
//
//        }
//    }
    suspend fun carPostRepo(request: CarPostRequest): Flow<ResultState<Response<CarPostResponse>>> =
        flow {
            emit(ResultState.Loading)
            try {
                val response = ApiProvider.api().carPost(request.toFieldMap())
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Error(e.message.toString()))
            }
        }

}