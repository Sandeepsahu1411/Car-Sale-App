package com.example.salecar.presentation_layer.screens.bottom_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.salecar.presentation_layer.screens.common_component.CustomTopBar

@Composable
fun ProfileScreenUI(navController: NavController) {
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Profile",
                navController = navController,
                showBackButton = false
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Profile Screen", style = MaterialTheme.typography.titleLarge)
        }

    }

}