package com.example.salecar.presentation_layer.screens.bottom_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.salecar.R
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.view_model.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishListScreenUI(navController: NavController,viewModel: AppViewModel) {

    val homeScreenState = viewModel.homeScreenState.collectAsState()

    val similarProduct = homeScreenState.value.data



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "WishList") },
                navigationIcon = { },
                actions = { },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            columns = GridCells.Fixed(2),
        ) {
            items(similarProduct.take(6)) { product ->
                ProductCard(product, onClick = {
                    navController.navigate(Routes.ProductDetailScreenRoute(id = product.id))

                }, isFavorite = true)
            }
        }
    }
}