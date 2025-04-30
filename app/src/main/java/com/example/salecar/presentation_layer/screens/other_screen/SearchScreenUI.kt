package com.example.salecar.presentation_layer.screens.other_screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.salecar.R
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.screens.common_component.CustomLoadingBar
import com.example.salecar.presentation_layer.view_model.AppViewModel

@Composable
fun SearchScreenUI(navController: NavController, viewModel: AppViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val homeScreenState = viewModel.homeScreenState.collectAsState()
    var search by remember { mutableStateOf("") }
    val filterProduct = homeScreenState.value.data.filter { product ->
        product.title.contains(search, ignoreCase = true)
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            CustomSearchBar(navController, search = search, onSearch = { search = it })

            if (search.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp, 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterProduct) {
                        val baseUrl = "https://aidot.uk/sellcar/"

                        val imageUrl =
                            if (it.images.isNotEmpty()) baseUrl + it.images.first() else ""

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clip(shape = RoundedCornerShape(10.dp))
                                .clickable{
                                    navController.navigate(Routes.ProductDetailScreenRoute(id = it.id))
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)

                        ) {
                            SubcomposeAsyncImage(
                                model = if (imageUrl.isNotEmpty()) imageUrl else R.drawable.car1,
                                contentDescription = null,
                                loading = {
                                    CustomLoadingBar(20.dp)
                                },
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(shape = RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Text(text = it.title, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                    if (filterProduct.isEmpty()) {
                        item {
                            Text(
                                text = "No results found",
                                modifier = Modifier.padding(vertical = 6.dp),
                                fontSize = 14.sp
                            )
                        }
                    }

                }

            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Search Product")
                }
            }

        }

    }
}

@Composable
fun CustomSearchBar(
    navController: NavController,
    search: String = "",
    onSearch: (String) -> Unit = {}
) {

    var isEnableClose by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = search,
            onValueChange = { onSearch(it) },
            enabled = true,
            singleLine = true,
            placeholder = {
                Text(text = "Search")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),

            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp)
                        .clickable {
                            navController.navigateUp()
                        }
                )
            },
            trailingIcon = {
                if (search.isNotEmpty()) {
                    isEnableClose = true
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp)
                            .clickable {
                                onSearch("")

                            }
                    )
                } else {
                    isEnableClose = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
    }


}

enum class Filter {
    Categories, Car, ForSale, Property, Tradespeople, HomeAndGarden, Jobs
}