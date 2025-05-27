package com.example.salecar.presentation_layer.screens.bottom_screen

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.salecar.R
import com.example.salecar.data_layer.api.IMAGE_BASEURL
import com.example.salecar.data_layer.response.profile_res.Listings
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.screens.bottom_screen.add_screen.CustomDivider
import com.example.salecar.presentation_layer.screens.common_component.CustomApiError
import com.example.salecar.presentation_layer.screens.common_component.CustomDialog
import com.example.salecar.presentation_layer.screens.common_component.CustomLoadingBar
import com.example.salecar.presentation_layer.screens.common_component.CustomOutlineButton
import com.example.salecar.presentation_layer.screens.common_component.rememberShimmerBrush
import com.example.salecar.presentation_layer.view_model.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenUI(
    navController: NavController, viewModel: AppViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val postListing = viewModel.postListingState.collectAsState()
    val carPostDeleteState = viewModel.deleteCarPostState.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }
    var postIdToDelete by remember { mutableStateOf<String?>(null) }

    CustomDialog(
        showDialog = showExitDialog,
        onDismiss = { showExitDialog = false },
        onConfirm = {
            postIdToDelete?.let {
                viewModel.deleteCarPost(it)
            }
            showExitDialog = false
            postIdToDelete = null
        },
        title = "Delete Post",
        message = "Are you sure you want to delete the post?",
        confirmButtonText = "Yes",
        cancelButtonText = "No"
    )
    when {
        carPostDeleteState.value.loading -> {
            CustomLoadingBar()
        }

        carPostDeleteState.value.error != null -> {
            Toast.makeText(context, carPostDeleteState.value.error, Toast.LENGTH_SHORT).show()
        }

        carPostDeleteState.value.data != null -> {
            Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show()

            carPostDeleteState.value.data = null

        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Profile") },
                navigationIcon = { },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Routes.SettingScreenRoute)
                    }) {
                        Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }) { innerPadding ->
        when {
            postListing.value.loading -> {
                ShimmerProfileScreen(innerPadding)
            }

            postListing.value.error != null -> {
                CustomApiError()
                Toast.makeText(context, postListing.value.error, Toast.LENGTH_SHORT).show()
            }

            postListing.value.data != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ProfileSec(userName = postListing.value.data?.body()?.username ?: "Na")
                    }
                    item {
                        Text(
                            text = "My Posts History",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(postListing.value.data?.body()?.listings ?: emptyList()) {
                        MyPostingCard(
                            data = it, navController, viewModel, onDelete = {
                                postIdToDelete =  it.id.toString()
                                showExitDialog = true

                            }
                        )

                    }// SPARK250

                }

            }
        }
    }

}


@Composable
fun ProfileSec(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userName.take(1),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

        }
        Text(
            text = userName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

    }
    CustomDivider()

}

@Composable
fun MyPostingCard(
    data: Listings,
    navController: NavController,
    viewModel: AppViewModel,
    onDelete: () -> Unit


) {

    val imageUrl =
        if (!data.images.isNullOrEmpty()) IMAGE_BASEURL + data.images.first() else R.drawable.no_image_avl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clickable {
                navController.navigate(Routes.ProductDetailScreenRoute(id = data.id.toString()))
            }, elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                loading = {
                    CustomLoadingBar()
                },
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top,

                    ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = data.title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "$${data.price}")
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        onDelete()


                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                CustomOutlineButton(
                    text = "Repost",
                    onClick = {},

                    )
            }

        }
    }


}

@Composable
fun ShimmerProfileScreen(innerPadding: PaddingValues = PaddingValues()) {
    val brush = rememberShimmerBrush()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(brush)
                )

            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .height(20.dp)
                    .width(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
        }
        items(4) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .fillMaxWidth(0.5f)
                                .background(brush)
                        )
                        Box(
                            modifier = Modifier
                                .height(16.dp)
                                .fillMaxWidth(0.3f)
                                .background(brush)
                        )
                        Box(
                            modifier = Modifier
                                .height(35.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(brush)
                        )
                    }
                }
            }

        }
    }

}
