package com.example.salecar.presentation_layer.screens.start_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.salecar.R
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.navigation.SubNavigation
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomTextField

@Composable
fun LoginScreenUI(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_car),
                contentDescription = "Splash",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f)
                    .padding(horizontal = 20.dp, 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    text = "Log In",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    Text(
                        text = "Email Address", style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholderText = "Enter Your Email",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Email,
                                contentDescription = null,
                            )
                        },
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next

                    )
                }
                Column {
                    Text(
                        text = "Password", style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholderText = "Enter Your Password",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = null,
                            )
                        },
                        isPassword = true,
                        keyboardType = KeyboardType.Password,

                        )
                }
                CustomButton(
                    text = "Continue",
                    onClick = {
                        navController.navigate(Routes.HomeScreenRoute){
                            popUpTo(SubNavigation.StartupScreen){
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Don't have an account? ",
                        fontSize = 18.sp,
                        color= MaterialTheme.colorScheme.onSecondary
                    )
                    Text(text = "Sign Up",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.SignUpScreenRoute)
                        })
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable {
                        navController.navigateUp()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.Black,

                    )
            }

        }
    }
}
