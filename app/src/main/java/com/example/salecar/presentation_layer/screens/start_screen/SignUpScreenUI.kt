package com.example.salecar.presentation_layer.screens.start_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.navigation.SubNavigation
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomTextField

@Composable
fun SignUpScreenUI(navController: NavController) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var textChecked by remember { mutableStateOf(false) }

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()

            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .wrapContentSize()
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        navController.navigateUp()
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.White,

                    )
            }
            Text(
                text = "Sing Up",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.weight(0.2f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Column {
                Text(
                    text = "First Name",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    color = MaterialTheme.colorScheme.onSecondary

                )
                CustomTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholderText = "Your First Name",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                        )
                    },
                    imeAction = ImeAction.Next

                )
            }
            Column {
                Text(
                    text = "Last Name",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    color = MaterialTheme.colorScheme.onSecondary

                )
                CustomTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholderText = "Enter Your Email",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                        )
                    },
                    imeAction = ImeAction.Next

                )
            }
            Column {
                Text(
                    text = "Email Address",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    color = MaterialTheme.colorScheme.onSecondary

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
                    text = "Password",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    color = MaterialTheme.colorScheme.onSecondary

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            ) {
                Checkbox(checked = textChecked, onCheckedChange = { textChecked = it })
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text =
                    "We will send you emails regarding our services, offers, competitions and carefully selected partners. These emails will always be sent by us and you can unsubscribe from receiving these marketing emails at any time.",
                    textAlign = TextAlign.Justify,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            ) {
                Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
                Spacer(modifier = Modifier.width(5.dp))
                Text(buildAnnotatedString {
                    append("I agree to the ")
                    pushStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary


                        )
                    )
                    append("Terms of Use")
                    pop()
                    append(" and the ")
                    pushStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    append("Privacy Notice")
                    pop()
                }, color = MaterialTheme.colorScheme.onSecondary, modifier = Modifier

                )
            }


            CustomButton(
                text = "Continue", onClick = {
                    if (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        navController.navigate(Routes.HomeScreenRoute) {
                            popUpTo(SubNavigation.StartupScreen) {
                                inclusive = true
                            }
                        }
                    } else {
                        Toast.makeText(context, "All fields are required !", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, modifier = Modifier.fillMaxWidth(), enabled = isChecked

            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(

                    text = "Already have an account? ", color = MaterialTheme.colorScheme.onSecondary , fontSize = 18.sp
                )
                Text(text = "Login",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.LoginScreeRoute)
                    })
            }
        }
    }
}