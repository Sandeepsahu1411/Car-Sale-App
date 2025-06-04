package com.example.salecar.presentation_layer.screens.other_screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.salecar.R
import com.example.salecar.data_layer.api.IMAGE_BASEURL
import com.example.salecar.data_layer.response.car_detail_res.CarData
import com.example.salecar.data_layer.response.home_res.Data
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.screens.bottom_screen.ProductCard
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomLoadingBar
import com.example.salecar.presentation_layer.screens.common_component.CustomTextField
import com.example.salecar.presentation_layer.screens.common_component.rememberShimmerBrush
import com.example.salecar.presentation_layer.view_model.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreenUI(navController: NavController, id: String?, viewModel: AppViewModel) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var message by remember { mutableStateOf("") }
    val carDetailState = viewModel.carDetailState.collectAsState()
    val data = carDetailState.value.data?.body()?.data

    val homeScreenState = viewModel.homeScreenState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.carDetail(id.toString())

    }

    Scaffold(

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            when {
                carDetailState.value.loading -> {
                    ShimmerProductDetailScreen(innerPadding)
                }

                carDetailState.value.error != null -> {
                    Toast.makeText(context, carDetailState.value.error, Toast.LENGTH_SHORT).show()
                }


                else -> {
                    val similarProduct = homeScreenState.value.data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding())
                            .padding(bottom = 70.dp),
                    ) {
                        item {
                            ProductDetail(data, navController, similarProduct)
                        }

                    }

                }
            }
            AnimatedTopBar(navController, scrollBehavior, data)
            if (carDetailState.value.data != null)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    data: CarData?
) {

    val context = LocalContext.current
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
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

                IconButton(onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out this car: ${data?.title}")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share Via")
                    context.startActivity(shareIntent)
                }) {
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
fun ProductDetail(data: CarData?, navController: NavController, product: List<Data>) {


    var isFavorite by remember { mutableStateOf(false) }


    ProductImageSlider(
        images = data?.images ?: emptyList(), baseUrl = IMAGE_BASEURL
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
                text = data?.title ?: "",
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
            text = "$${data?.price ?: ""} ",
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
                text = data?.address ?: "New Delhi",
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
            text = data?.description ?: "", style = MaterialTheme.typography.bodyLarge
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

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(). height (630.dp),
        columns = GridCells.Fixed(2),
    ) {

        items(product.take(6)) { product ->
            ProductCard(product, onClick = {
                navController.navigate(Routes.ProductDetailScreenRoute(id = product.id))
            })
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImageSlider(images: List<String>, baseUrl: String) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    Column {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            pageSpacing = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) { pageIndex ->
            SubcomposeAsyncImage(
                model = if (images.isNotEmpty()) baseUrl + images[pageIndex] else R.drawable.no_image_avl,
                contentDescription = null,
                loading = { CustomLoadingBar() },
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(images.size) { index ->
                val isSelected = pagerState.currentPage == index
                item {
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 40.dp else 35.dp)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }


                            }

                    ) {
                        SubcomposeAsyncImage(
                            model = if (images.isNotEmpty()) baseUrl + images[index] else R.drawable.no_image_avl,
                            contentDescription = null,
                            loading = { CustomLoadingBar(10.dp) },
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                }
            }
        }

    }
}

@Composable
fun ShimmerProductDetailScreen(innerPadding: PaddingValues = PaddingValues()) {
    val brush = rememberShimmerBrush()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = innerPadding.calculateTopPadding()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
                .background(brush)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(40.dp)
                        .padding(horizontal = 5.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(brush)
                )

            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            Modifier.padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Box(
                modifier = Modifier
                    .height(30.dp)
                    .width(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )

            Box(
                modifier = Modifier
                    .height(30.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
            Row(
                Modifier.padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )

            }
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )


            }
        }
    }
}
