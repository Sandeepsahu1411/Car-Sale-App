package com.example.salecar.presentation_layer.screens.other_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.salecar.R
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.screens.bottom_screen.Product
import com.example.salecar.presentation_layer.screens.bottom_screen.ProductCard
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreenUI(navController: NavController) {
    val product = navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var message by remember { mutableStateOf("") }

    product?.let { product ->
        Scaffold(

        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                        .padding(bottom = 70.dp),
                ) {
                    item {
                        ProductDetail(product, navController)
                    }

                }
                AnimatedTopBar(navController, scrollBehavior)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .align(Alignment.BottomCenter)
                        .padding(10.dp),
                    contentAlignment = Alignment.BottomCenter

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)

                    ) {
                        CustomTextField(
                            value = message,
                            onValueChange = { message = it },
                            modifier = Modifier.weight(0.7f),
                            placeholderText = "Is this still available? "
                        )
                        CustomButton(
                            onClick = {}, text = "Send", modifier = Modifier.weight(0.3f)
                        )

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTopBar(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {

    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
    ), title = { }, navigationIcon = {
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }, actions = {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {

            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }, scrollBehavior = scrollBehavior
    )


}

@Composable
fun ProductDetail(it: Product, navController: NavController) {

    var isFavorite by remember { mutableStateOf(false) }

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

    Image(
        painter = painterResource(id = it.imageRes),
        contentDescription = "Product Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = it.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                lineHeight = 25.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                }, modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Back",
                    tint = Color.Red,
                )
            }

        }

        Text(
            text = it.price,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSecondary,
            )
            Text(
                text = "Noida-63",
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline
                ),
                lineHeight = 20.sp,
            )
        }
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n \n" + " ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Posted 2 Week ago",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "S",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)

                )
            }
            Column {
                Text(
                    text = "Seller Name",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Seller Location",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { }, modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }


        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        )
        Column {
            Text(
                text = "Member since 2012 | Active today",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "Email Address verified",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Text(
            text = "Similar Products",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )


    }
    Box(
        modifier = Modifier
            .height(650.dp)
            .fillMaxWidth()
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
        ) {
            items(dummyProducts.take(6)) { product ->
                ProductCard(product, onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("product", it)
                    navController.navigate(Routes.ProductDetailScreenRoute)
                })
            }
        }
    }
}