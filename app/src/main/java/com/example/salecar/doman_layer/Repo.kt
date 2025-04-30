package com.example.salecar.doman_layer

import android.util.Log
import android.util.Log.e
import com.example.salecar.ResultState
import com.example.salecar.data_layer.api.ApiProvider
import com.example.salecar.data_layer.response.car_detail_res.CarDetailResponse
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

}