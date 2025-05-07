package com.example.salecar.presentation_layer.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.salecar.preference_db.UserPreferenceManager
import com.example.salecar.presentation_layer.screens.bottom_screen.add_screen.AddScreenUI
import com.example.salecar.presentation_layer.screens.bottom_screen.HomeScreenUI
import com.example.salecar.presentation_layer.screens.bottom_screen.MassageScreenUI
import com.example.salecar.presentation_layer.screens.bottom_screen.ProfileScreenUI
import com.example.salecar.presentation_layer.screens.bottom_screen.WishListScreenUI
import com.example.salecar.presentation_layer.screens.common_component.CustomBottomBar
import com.example.salecar.presentation_layer.screens.other_screen.NotificationScreenUI
import com.example.salecar.presentation_layer.screens.other_screen.ProductDetailScreenUI
import com.example.salecar.presentation_layer.screens.other_screen.SearchScreenUI
import com.example.salecar.presentation_layer.screens.start_screen.LoginScreenUI
import com.example.salecar.presentation_layer.screens.start_screen.SignUpScreenUI
import com.example.salecar.presentation_layer.screens.start_screen.SplashScreenUI
import com.example.salecar.presentation_layer.screens.start_screen.StartScreenUI
import com.example.salecar.presentation_layer.view_model.AppViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val userPreferenceManager = UserPreferenceManager(LocalContext.current)
    val viewModel : AppViewModel = hiltViewModel()
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    var bottomBarVisible by remember { mutableStateOf(false) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(currentBackStackEntry?.destination?.route) {
        val currentRoute = currentBackStackEntry?.destination?.route
        bottomBarVisible = currentRoute !in listOf(
            Routes.SplashScreenRoute::class.qualifiedName,
            Routes.StartScreenRoute::class.qualifiedName,
            Routes.LoginScreeRoute::class.qualifiedName,
            Routes.SignUpScreenRoute::class.qualifiedName,
            Routes.NotificationScreenRoute::class.qualifiedName,
            Routes.AddScreenRoute::class.qualifiedName,
            Routes.SearchScreenRoute::class.qualifiedName,
            Routes.ProductDetailScreenRoute::class.qualifiedName
        )
    }
    Scaffold(containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AnimatedVisibility(visible = bottomBarVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 200)) + slideInVertically { it },
                exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically { it }) {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.AddScreenRoute) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape,
                    modifier = Modifier.offset(y = 50.dp),
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(3.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, "Fab")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            AnimatedVisibility(visible = bottomBarVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 200)) + slideInVertically { it },
                exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + slideOutVertically { it }) {
                CustomBottomBar(selectedItemIndex = selectedItemIndex, onItemSelected = { index ->
                    selectedItemIndex = index
                    val route = when (index) {
                        0 -> Routes.HomeScreenRoute
                        1 -> Routes.WishListScreenRoute
                        2 -> Routes.MassageScreenRoute
                        3 -> Routes.ProfileScreenRoute
                        else -> Routes.HomeScreenRoute
                    }
                    navController.navigate(route) {
                        popUpTo(route) { inclusive = true }
                    }
                })
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (bottomBarVisible) innerPadding.calculateBottomPadding() else 0.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            NavHost(
                navController = navController, startDestination = SubNavigation.StartupScreen,
            ) {
                navigation<SubNavigation.StartupScreen>(startDestination = Routes.SplashScreenRoute) {
                    composable<Routes.SplashScreenRoute> {
                        SplashScreenUI(navController, userPreferenceManager)
                    }
                    composable<Routes.StartScreenRoute> {
                        StartScreenUI(navController)
                    }
                    composable<Routes.LoginScreeRoute> {
                        LoginScreenUI(navController)
                    }
                    composable<Routes.SignUpScreenRoute> {
                        SignUpScreenUI(navController)
                    }

                }
                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreenRoute) {
                    composable<Routes.HomeScreenRoute> {
                        HomeScreenUI(navController)
                    }
                    composable<Routes.MassageScreenRoute> {
                        MassageScreenUI(navController)
                    }
                    composable<Routes.WishListScreenRoute> {
                        WishListScreenUI(navController)
                    }
                    composable<Routes.ProfileScreenRoute> {
                        ProfileScreenUI(navController,userPreferenceManager)
                    }
                    composable<Routes.NotificationScreenRoute> {
                        NotificationScreenUI(navController)
                    }
                    composable<Routes.AddScreenRoute> {
                        AddScreenUI(navController)
                    }
                    composable<Routes.SearchScreenRoute> {
                        SearchScreenUI(navController)
                    }
                    composable<Routes.ProductDetailScreenRoute> {
                        val id = it.arguments?.getString("id")
                        ProductDetailScreenUI(navController,id,viewModel)
                    }

                }
            }
        }
    }
}

