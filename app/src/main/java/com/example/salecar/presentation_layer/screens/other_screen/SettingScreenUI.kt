package com.example.salecar.presentation_layer.screens.other_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.salecar.preference_db.UserPreferenceManager
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.navigation.SubNavigation
import com.example.salecar.presentation_layer.screens.bottom_screen.add_screen.CustomDivider
import com.example.salecar.presentation_layer.screens.common_component.CustomOutlineButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenUI(
    navController: NavController,
    userPreferenceManager: UserPreferenceManager,
) {
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Settings",
                    fontSize = 20.sp,
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )

                }
            },
            actions = { },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
                .padding(vertical = 10.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingRows(title = "My Account", subTitle = "Account Settings", onClick = {})
            SettingRows(subTitle = "Privacy Settings", onClick = {})
            SettingRows(title = "Notifications", subTitle = "Settings", onClick = {})
            SettingRows(title = "Help", subTitle = "Help & Contact", onClick = { })
            SettingRows(subTitle = "Share app", onClick = {})
            SettingRows(title = "Legal", subTitle = "Terms of use", onClick = {})
            SettingRows(subTitle = "privacy notice", onClick = {})
            SettingRows(subTitle = "Legal information", onClick = {})

            CustomOutlineButton(
                text = "Logout",
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.padding(10.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(text = "copyright © 2023 Sale Car", style = MaterialTheme.typography.labelLarge)
            Text(text = "Sale app version v10.0.12", style = MaterialTheme.typography.labelMedium)

        }
        LogoutDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onLogout = {
                showDialog = false
                scope.launch {
//                    withContext(Dispatchers.IO) {
                        userPreferenceManager.clearUserID()

                    navController.navigate(Routes.StartScreenRoute) {
                        popUpTo(SubNavigation.MainHomeScreen) { inclusive = true }
                    }
                }
            },

            )

    }

}

@Composable
fun SettingRows(title: String = "", subTitle: String, onClick: () -> Unit) {

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        if (title.isNotEmpty())
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { onClick() }
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = subTitle, modifier = Modifier.weight(1f))
            IconButton(onClick = { onClick() }) {
                Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null)
            }
        }
    }
    CustomDivider(
        thickness = 2.dp,

    )

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