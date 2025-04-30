package com.example.salecar.presentation_layer.screens.start_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.salecar.preference_db.UserPreferenceManager
import com.example.salecar.presentation_layer.navigation.Routes
import kotlinx.coroutines.delay
import com.example.salecar.R


@Composable
fun SplashScreenUI(navController: NavHostController, userPreferenceManager: UserPreferenceManager) {

    var token by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit) {
        userPreferenceManager.userID.collect {
            if (it != null) {
                token = it
            }
        }
    }
    LaunchedEffect(key1 = true) {
        delay(3000)
        if (token.isNotBlank()) {
            navController.navigate(Routes.HomeScreenRoute) {

                popUpTo(Routes.SplashScreenRoute) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Routes.StartScreenRoute) {
                popUpTo(Routes.SplashScreenRoute) {
                    inclusive = true
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.car_img),
            contentDescription = "Splash",
            modifier = Modifier.size(250.dp),
            contentScale = ContentScale.Crop
        )
    }

}