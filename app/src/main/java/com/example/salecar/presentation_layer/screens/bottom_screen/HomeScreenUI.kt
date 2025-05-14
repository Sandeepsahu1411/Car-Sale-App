package com.example.salecar.presentation_layer.screens.bottom_screen

import android.os.Parcelable
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.salecar.R
import com.example.salecar.data_layer.response.home_res.Data
import com.example.salecar.data_layer.response.home_res.HomeScreenResponse
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.screens.common_component.CustomLoadingBar
import com.example.salecar.presentation_layer.screens.common_component.rememberShimmerBrush
import com.example.salecar.presentation_layer.view_model.AppViewModel
import kotlinx.parcelize.Parcelize

@Composable
fun HomeScreenUI(navController: NavController, viewModel: AppViewModel = hiltViewModel()) {
    val categories = listOf(
        "All", "Categories", "Car", "ForSale", "Property", "Tradespeople", "HomeAndGarden", "Jobs"
    )
    val homeScreenState = viewModel.homeScreenState.collectAsState()


    var selectedCategory by remember { mutableStateOf("All") }
    val listState = rememberLazyGridState().apply { layoutInfo }
    val context = LocalContext.current
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
            when {
                homeScreenState.value.loading -> {
                    ShimmerHomeGridItem()
                }

                homeScreenState.value.error != null -> {
                    Toast.makeText(context, homeScreenState.value.error, Toast.LENGTH_SHORT).show()
                }

                else -> {

                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        columns = GridCells.Fixed(2),


                        ) {
                        items(homeScreenState.value.data) { product ->
                            ProductCard(product, onClick = {
                                navController.navigate(Routes.ProductDetailScreenRoute(id = product.id))
                            })
                        }

                    }
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
                InputChip(
                    selected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    label = { Text(text = category) })
            }
        }
    }
}

@Composable
fun ProductCard(product: Data, onClick: (Data) -> Unit, isFavorite: Boolean = false) {
    val baseUrl = "https://aidot.uk/sellcar/"
    val imageUrl =
        if (!product.images.isNullOrEmpty()) baseUrl + product.images.first() else R.drawable.no_image_avl

    var isFavorite by rememberSaveable { mutableStateOf(isFavorite) }
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
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                loading = {
                    CustomLoadingBar()
                },
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
                    .clickable {
                        isFavorite = !isFavorite
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (!isFavorite) Icons.Rounded.FavoriteBorder else Icons.Rounded.Favorite,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp),
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
                text = product.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )

            Text(
                text = " $${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ShimmerHomeGridItem() {
    val brush = rememberShimmerBrush()

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(8) {


            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth(0.6f)
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(0.4f)
                        .background(brush)
                )
            }
        }
    }
}
