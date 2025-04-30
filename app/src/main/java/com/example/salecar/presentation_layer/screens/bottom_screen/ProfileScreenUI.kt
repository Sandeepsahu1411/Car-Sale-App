package com.example.salecar.presentation_layer.screens.bottom_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.salecar.preference_db.UserPreferenceManager
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.navigation.SubNavigation
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomTopBar
import kotlinx.coroutines.launch

@Composable
fun ProfileScreenUI(navController: NavController, userPreferenceManager: UserPreferenceManager) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        userPreferenceManager.userID.collect { userId ->
            Log.d("UserPreference", "User ID from UserPreferenceManager: $userId")
        }
    }
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Profile",
                navController = navController,
                showBackButton = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Profile Screen", style = MaterialTheme.typography.titleLarge)

            CustomButton(
                text = "Logout",
                onClick = {
                    scope.launch {
                        userPreferenceManager.clearUserID()
                        navController.navigate(Routes.StartScreenRoute) {
                            popUpTo(SubNavigation.MainHomeScreen) { inclusive = true }
                        }
                    }


                }
            )
        }

    }

}