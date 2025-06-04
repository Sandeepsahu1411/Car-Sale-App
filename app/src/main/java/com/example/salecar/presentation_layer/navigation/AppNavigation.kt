package com.example.salecar.presentation_layer.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.salecar.presentation_layer.screens.other_screen.SettingScreenUI
import com.example.salecar.presentation_layer.screens.start_screen.LoginScreenUI
import com.example.salecar.presentation_layer.screens.start_screen.SignUpScreenUI
import com.example.salecar.presentation_layer.screens.start_screen.SplashScreenUI
import com.example.salecar.presentation_layer.screens.start_screen.StartScreenUI
import com.example.salecar.presentation_layer.view_model.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val userPreferenceManager = UserPreferenceManager(LocalContext.current)
    val viewModel: AppViewModel = hiltViewModel()
    val networkStatus = viewModel.isConnected.collectAsState()



    val bottomBarRoutes = listOf(
        Routes.HomeScreenRoute::class.qualifiedName,
        Routes.WishListScreenRoute::class.qualifiedName,
        Routes.MassageScreenRoute::class.qualifiedName,
        Routes.ProfileScreenRoute::class.qualifiedName,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarVisible = remember(currentRoute) {
        mutableStateOf(currentRoute in bottomBarRoutes)
    }
    var selectedItemIndex by remember(currentRoute) {
        mutableIntStateOf(bottomBarRoutes.indexOf(currentRoute).takeIf { it >= 0 } ?: 0)
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (bottomBarVisible.value) {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.AddScreenRoute(id = null)) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape,
                    modifier = Modifier.offset(y = 50.dp),
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(3.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
        },

        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            AnimatedContent(
                targetState = bottomBarVisible,
                transitionSpec = {
                    fadeIn(animationSpec = tween(200, easing = FastOutSlowInEasing)) with
                            fadeOut(animationSpec = tween(300, easing = FastOutSlowInEasing))
                },
                label = "BottomBarTransition"
            ) { visible ->
                if (visible.value) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                    ) {
                        CustomBottomBar(
                            selectedItemIndex = selectedItemIndex,
                            onItemSelected = { index ->
                                selectedItemIndex = index
                                val route = when (index) {
                                    0 -> Routes.HomeScreenRoute
                                    1 -> Routes.WishListScreenRoute
                                    2 -> Routes.MassageScreenRoute
                                    3 -> Routes.ProfileScreenRoute
                                    else -> Routes.HomeScreenRoute
                                }
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                        if (!networkStatus.value) {
                            ConnectionBanner()
                        }
                    }
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (bottomBarVisible.value) innerPadding.calculateBottomPadding() else 0.dp),
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
                        WishListScreenUI(navController, viewModel)
                    }
                    composable<Routes.ProfileScreenRoute> {
                        ProfileScreenUI(navController,viewModel,userPreferenceManager)
                    }
                    composable<Routes.NotificationScreenRoute> {
                        NotificationScreenUI(navController)
                    }
                    composable<Routes.AddScreenRoute> {
                        val id = it.arguments?.getString("id")
                        AddScreenUI(navController,id)
                    }
                    composable<Routes.SearchScreenRoute> {
                        SearchScreenUI(navController)
                    }
                    composable<Routes.ProductDetailScreenRoute> {
                        val id = it.arguments?.getString("id")
                        ProductDetailScreenUI(navController, id, viewModel)
                    }
                    composable<Routes.SettingScreenRoute> {
                        SettingScreenUI(navController, userPreferenceManager)
                    }

                }
            }
        }
    }
}

@Composable
fun ConnectionBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No connection",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

