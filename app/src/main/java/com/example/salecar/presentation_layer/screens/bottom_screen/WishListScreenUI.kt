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
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.salecar.R
import com.example.salecar.presentation_layer.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishListScreenUI(navController: NavController) {


    val dummyProducts = listOf(
        Product(R.drawable.car1, "Hyundai Creta", "₹5,00,000"),
        Product(R.drawable.car2, "Swift Dzire", "₹7,20,000"),
        Product(R.drawable.car3, "Grand i10", "₹3,50,000"),
        Product(R.drawable.car4, "Alto K10 9001B", "₹6,75,000"),
        Product(R.drawable.car5, "Mahindra Scorpio", "₹4,90,000"),
        Product(R.drawable.car6, "Maruti Suzuki Baleno", "₹8,40,000"),
        Product(R.drawable.car7, "Toyota Fortuner Black", "₹2,80,000"),
        Product(R.drawable.car8, "Mahindra Thar Black", "₹5,50,000"),
        Product(R.drawable.car9, "Tata Safari", "₹7,90,000"),
        Product(R.drawable.car10, "Tata Ace", "₹9,30,000"),
        )

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
            items(dummyProducts.take(6)) { product ->
                ProductCard(product, onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("product", it)
                    navController.navigate(Routes.ProductDetailScreenRoute)
                }, isFavorite = true)
            }
        }
    }
}