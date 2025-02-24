package com.example.salecar.presentation_layer.screens.start_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.salecar.R
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.screens.common_component.CustomBottomSheet
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomOutlineButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreenUI(navController: NavController) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isSignUp by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.start_screen_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, Color.Black
                        ),
                        startY = 1500f,

                        )
                )
        )

        Column(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Find what You need ,\n wherever you are",
                textAlign = TextAlign.Center,
                lineHeight = 34.sp,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            CustomButton(
                text = "Log In",
                onClick = {
                    isSignUp = false
                    showBottomSheet = true
                },
            )
            CustomButton(text = "Sign Up", onClick = {
                isSignUp = true
                showBottomSheet = true
            })
        }
    }

    CustomBottomSheet(
        showSheet = showBottomSheet,
        onDismiss = { showBottomSheet = false }
    ) {
        CustomOutlineButton(
            text = "Login with Google",
            iconRes = R.drawable.google,
            onClick = { })
        CustomOutlineButton(
            text = "Login with Facebook",
            iconRes = R.drawable.facebook,
            onClick = { })
        CustomOutlineButton(
            text = if (isSignUp) "Sign up with email" else "Continue with email",
            onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                        navController.navigate(
                            if (isSignUp) Routes.SignUpScreenRoute else Routes.LoginScreeRoute
                        )
                    }
                }
            },
            border = null
        )
    }
}