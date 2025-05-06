package com.example.salecar.presentation_layer.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salecar.ResultState
import com.example.salecar.data_layer.response.car_detail_res.CarDetailResponse
import com.example.salecar.data_layer.response.car_post_res.CarPostRequest
import com.example.salecar.data_layer.response.car_post_res.CarPostResponse
import com.example.salecar.data_layer.response.home_res.Data
import com.example.salecar.data_layer.response.home_res.HomeScreenResponse

import com.example.salecar.data_layer.response.login_res.LoginResponse
import com.example.salecar.data_layer.response.signup_res.SignUpResponse
import com.example.salecar.doman_layer.Repo
import com.example.salecar.preference_db.UserPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repo: Repo, private val userPreferenceManager: UserPreferenceManager
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    private val _carDetailState = MutableStateFlow(CarDetailState())
    val carDetailState = _carDetailState.asStateFlow()

    private val _carPostState = MutableStateFlow(CarPostState())
    val carPostState = _carPostState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.loginRepo(email, password).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _loginState.value = LoginState(loading = true)
                    }

                    is ResultState.Success -> {
                        val admin = it.data.body()
                        if (admin != null) {
                            userPreferenceManager.saveUserID(admin.id.toString())
                            Log.d(
                                "UserPreference",
                                "User ID saved to UserPreferenceManager: ${admin.id}"
                            )
                        }
                        _loginState.value = LoginState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _loginState.value = LoginState(error = it.message)
                    }
                }
            }

        }


    }

    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.signUpRepo(username, email, password).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _signUpState.value = SignUpState(loading = true)
                    }

                    is ResultState.Success -> {
                        val userId = it.data.body()
                        if (userId != null) {
                            userPreferenceManager.saveUserID(userId.id.toString())
                        }

                        _signUpState.value = SignUpState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _signUpState.value = SignUpState(error = it.message)
                    }
                }
            }
        }
    }

    init {
        getHomeScreen()
    }

    fun getHomeScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getHomeScreen().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _homeScreenState.value = HomeScreenState(loading = true)
                    }

                    is ResultState.Success -> {
                        val body = it.data.body()
                        if (body?.status == "success") {
                            _homeScreenState.value = HomeScreenState(data = body.data)
                        } else {
                            _homeScreenState.value =
                                HomeScreenState(error = "API Status not success")
                        }
                    }

                    is ResultState.Error -> {
                        _homeScreenState.value = HomeScreenState(error = it.message)
                    }
                }
            }
        }
    }

    fun carDetail(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.carDetailRepo(id).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _carDetailState.value = CarDetailState(loading = true)
                    }

                    is ResultState.Success -> {
                        _carDetailState.value = CarDetailState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _carDetailState.value = CarDetailState(error = it.message)
                    }
                }
            }
        }
    }


    fun carPost(request: CarPostRequest, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.carPostRepo(request , context).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _carPostState.value = CarPostState(loading = true)
                    }

                    is ResultState.Success -> {
                        _carPostState.value = CarPostState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _carPostState.value = CarPostState(error = it.message)
                    }
                }
            }
        }

    }

}

data class LoginState(
    val loading: Boolean = false,
    var error: String? = null,
    var data: Response<LoginResponse>? = null,
)

data class SignUpState(
    val loading: Boolean = false,
    var error: String? = null,
    var data: Response<SignUpResponse>? = null,
)

data class HomeScreenState(
    val loading: Boolean = false,
    var error: String? = null,
    var data: List<Data> = emptyList()
)

data class CarDetailState(
    val loading: Boolean = false,
    var error: String? = null,
    var data: Response<CarDetailResponse>? = null,
)

data class CarPostState(
    val loading: Boolean = false,
    var error: String? = null,
    var data: Response<CarPostResponse>? = null,

    )