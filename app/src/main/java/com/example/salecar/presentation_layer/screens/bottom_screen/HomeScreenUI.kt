package com.example.salecar.presentation_layer.screens.bottom_screen

import android.os.Parcelable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.salecar.R
import com.example.salecar.presentation_layer.navigation.Routes
import kotlinx.parcelize.Parcelize

@Composable
fun HomeScreenUI(navController: NavController) {
    val categories = listOf(
        "All", "Categories", "Car", "ForSale", "Property", "Tradespeople", "HomeAndGarden", "Jobs"
    )
    var selectedCategory by remember { mutableStateOf("All") }
    val listState = rememberLazyGridState().apply { layoutInfo }

    var isScrollingUp by remember { mutableStateOf(true) }
    var lastScrollIndex by remember { mutableIntStateOf(1) }

    LaunchedEffect(remember { derivedStateOf { listState.firstVisibleItemIndex } }) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { currentIndex ->
            isScrollingUp = currentIndex < lastScrollIndex
            lastScrollIndex = currentIndex
        }
    }
    val categoryBarHeight by animateDpAsState(
        targetValue = if (isScrollingUp) 40.dp else 0.dp,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )
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
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SearchBarRow(navController)

            CategoryBar(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                categoryBarHeight = categoryBarHeight
            )
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
//                    .animateContentSize()
                state = listState,
                columns = GridCells.Fixed(2),
//                userScrollEnabled = true

            ) {
                items(dummyProducts) { product ->
                    ProductCard(product, onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("product", it)
                        navController.navigate(Routes.ProductDetailScreenRoute)
                    })
                }
                items(dummyProducts) { product ->
                    ProductCard(product, onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("product", it)
                        navController.navigate(Routes.ProductDetailScreenRoute)
                    })
                }
            }
        }

    }
}

@Composable
fun SearchBarRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                navController.navigate(Routes.SearchScreenRoute)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 10.dp)
                .size(30.dp)
        )
        Text(
            text = "Search",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(15.dp)
        )

    }

}

@Composable
fun CategoryBar(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categoryBarHeight: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(categoryBarHeight)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                InputChip(selected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    label = { Text(text = category) })
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: (Product) -> Unit) {
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .height(210.dp)
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(product) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.TopEnd


        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .shadow(elevation = 10.dp)
                    .clickable{
                        isFavorite = !isFavorite
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (!isFavorite)Icons.Rounded.FavoriteBorder else Icons.Rounded.Favorite, contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        ,
                    tint = Color.Red

                )

            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )

            Text(
                text = product.price,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Parcelize
data class Product(
    val imageRes: Int, val name: String, val price: String
) : Parcelable