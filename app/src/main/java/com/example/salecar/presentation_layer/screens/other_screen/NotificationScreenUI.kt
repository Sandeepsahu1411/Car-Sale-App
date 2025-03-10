package com.example.salecar.presentation_layer.screens.other_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.salecar.presentation_layer.screens.common_component.CustomTopBar
import kotlin.math.min
import com.example.salecar.R
import kotlin.math.max

@Composable
fun NotificationScreenUI(navController: NavController) {

    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            StickyHeaderScreen()
        }

    }
}


@Composable
fun StickyHeaderScreen() {
    val scrollState = rememberLazyListState()
    val scrollOffset = remember {
        derivedStateOf {
            val offset = scrollState.firstVisibleItemScrollOffset / 600f
            min(1f, max(0f, offset)) // Ensure value stays between 0 and 1
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(lerp(Color.Transparent, Color.Black, scrollOffset.value))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Header", color = Color.White, fontSize = 20.sp)
        }

        LazyColumn(state = scrollState) {
            item {
                Image(
                    painter = rememberImagePainter("https://source.unsplash.com/random/800x600"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            items(List(50) { "Item $it" }) { item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    fontSize = 18.sp
                )
            }
        }
    }
}
