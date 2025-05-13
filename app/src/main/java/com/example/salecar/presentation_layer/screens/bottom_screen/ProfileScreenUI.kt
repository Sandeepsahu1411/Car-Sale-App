package com.example.salecar.presentation_layer.screens.bottom_screen

import android.R.attr.data
import android.R.attr.text
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.salecar.R
import com.example.salecar.preference_db.UserPreferenceManager
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.navigation.SubNavigation
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomLoadingBar
import com.example.salecar.presentation_layer.screens.common_component.CustomTopBar
import com.example.salecar.presentation_layer.view_model.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreenUI(
    navController: NavController,
    userPreferenceManager: UserPreferenceManager,
    viewModel: AppViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val postListing = viewModel.postListingState.collectAsState()


    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Profile", navController = navController, showBackButton = false
            )
        }) { innerPadding ->
        when {
            postListing.value.loading -> {
                CustomLoadingBar()
            }

            postListing.value.error != null -> {
                Toast.makeText(context, postListing.value.error, Toast.LENGTH_SHORT).show()
            }

            postListing.value.data != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text(text = postListing.value.data?.body()?.username ?: "Na")
                    }
                    items(postListing.value.data?.body()?.listings ?: emptyList()) {
                        Text(text = it.title)
                    }

                    item {
                        CustomButton(
                            text = "Logout", onClick = {
                                showDialog = true

                            }, modifier = Modifier.padding(horizontal = 26.dp)
                        )
                    }
                }

            }
        }
        LogoutDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onLogout = {
                showDialog = false
                scope.launch {
                    withContext(Dispatchers.IO) {
                        userPreferenceManager.clearUserID()
                    }
                    navController.navigate(Routes.StartScreenRoute) {
                        popUpTo(SubNavigation.MainHomeScreen) { inclusive = true }
                    }
                }
            },

            )
    }

}

@Composable
fun LogoutDialog(
    showDialog: Boolean, onDismiss: () -> Unit, onLogout: () -> Unit
) {
    if (showDialog) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            AlertDialog(
                onDismissRequest = onDismiss, confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Cancel", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Log Out", fontWeight = FontWeight.Bold, color = Color.White
                        )
                    }
                }
            }, title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(
                        text = "LOG OUT",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }, text = {
                Text(
                    text = "Do you Really Want To Logout!",
                    textAlign = TextAlign.Center,
                    color = if (isSystemInDarkTheme()) Color(0xFFEFCECE) else Color.DarkGray,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }, shape = RoundedCornerShape(20.dp)

            )
        }
    }
}

