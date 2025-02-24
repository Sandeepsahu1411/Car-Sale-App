package com.example.salecar.presentation_layer.screens.other_screen

import android.R.attr.contentDescription
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SearchScreenUI(navController: NavController) {




    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            CustomSearchBar(navController)


        }

    }
}

@Composable
fun CustomSearchBar(navController: NavController) {
    var search by remember { mutableStateOf("") }
    var isEnableClose by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = search,
            onValueChange = { search = it },
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
                                search = ""
                            }
                    )
                } else {
                    isEnableClose = false
                }
            },
            modifier = Modifier.fillMaxWidth()
                .focusRequester(focusRequester)
        )
    }


}

enum class Filter {
    Categories, Car, ForSale, Property, Tradespeople, HomeAndGarden, Jobs
}