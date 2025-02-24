package com.example.salecar.presentation_layer.screens.common_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.salecar.presentation_layer.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    navController: NavController,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    showBackButton: Boolean = true,
    actionShow: Boolean = true,
    navigationIcon: ImageVector = Icons.Default.Menu,
    actionsContent: @Composable RowScope.() -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = if (actionShow) TextAlign.Center else TextAlign.Start

            )
        },
        actions = {
            actionsContent()
            if (actionShow) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate(Routes.NotificationScreenRoute)
                        }
                )
            } else {
                Spacer(modifier = Modifier.size(30.dp))
            }
        },
        navigationIcon = {
            Icon(
                imageVector = if (showBackButton) Icons.Default.ArrowBack else navigationIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (showBackButton) {
                            navController.navigateUp()
                        } else {
                            coroutineScope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }
                    }
            )
        },
    )
}
