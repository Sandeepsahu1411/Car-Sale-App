package com.example.salecar.presentation_layer.screens.bottom_screen

import android.R.attr.bottom
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.salecar.R
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreenUI(navController: NavController) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Sell Your Item",
                    fontSize = 20.sp,
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )

                }
            },
            actions = { },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        )
    }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            ) {
            item {
                AddPhotosSec()
            }
            item {
                VehicleSpecificationSec()
            }
            item {
                AddCarDetails()
            }
            item {
                ContactDetailSec()
            }
            item {
                LocationDetail()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhotosSec() {
    val imageUris = remember { mutableStateListOf<Uri>() }
    val maxImages = 20
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { selectedUris ->
        val newImages = selectedUris.take(maxImages - imageUris.size)
        imageUris.addAll(newImages)
    }

    Column(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Add Photos", fontSize = 18.sp, fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.idea),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
            Text(
                text = "Ads with good pictures get more views and replies.",
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
        }

        val totalItems = imageUris.size + if (imageUris.size < maxImages) 1 else 0
        val rows = (totalItems + 2) / 3
        val gridHeight = (rows * 110).dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (imageUris.isEmpty()) 120.dp else gridHeight),
            contentAlignment = if (imageUris.isEmpty()) Alignment.Center else Alignment.TopStart
        ) {
            if (imageUris.isEmpty()) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
                        .clickable {
                            launcher.launch("image/*")
                        }, contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.upload_img),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                }
            } else {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    userScrollEnabled = false
                ) {
                    items(imageUris.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUris[index]),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            Icon(imageVector = Icons.Default.Close,
                                contentDescription = "Remove Image",
                                tint = Color.Red,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(Color.White, CircleShape)
                                    .clickable {
                                        imageUris.removeAt(index)
                                    })
                        }
                    }

                    if (imageUris.size < maxImages) {
                        item {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
                                    .clickable {
                                        launcher.launch("image/*")
                                    }, contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.upload_img),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = "${imageUris.size}/$maxImages images uploaded",
            fontSize = 16.sp,
            color = if (imageUris.size < maxImages) Color.Gray else Color.Red,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

    HorizontalDivider(
        thickness = 1.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 10.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSpecificationSec() {

    var plateNo by remember { mutableStateOf("") }

    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedFeatures = remember { mutableStateListOf<String>() }
    val featureList = listOf(
        "AUX/USB Input Socket",
        "Adjustable Steering Wheel",
        "Air Conditioning",
        "Airbag Knee Driver",
        "Alarm System/Remote Anti-Theft",
        "Alloy Wheels",
        "Android Auto",
        "Anti-lock Braking",
        "Apple Car Play",
        "Automatic Air Con/Climate Control",
        "Automatic Headlights with Dusk Sensor",
        "Automatic Stop/Start",
        "Bluetooth Connectivity"
    )

    Column(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Vehicle Specification", fontSize = 18.sp, fontWeight = FontWeight.Bold
        )
        Column {
            Text(
                text = "Enter the licence plate number",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(5.dp))
            CustomTextField(value = plateNo,
                onValueChange = { plateNo = it.uppercase() },
                placeholderText = "Enter REG",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.car_number_plate),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                })
        }
        CustomButton(
            onClick = { }, text = "Look up details", enabled = plateNo.isNotBlank()
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = "Vehicle standard features", fontSize = 18.sp, fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
//                .background(Color.LightGray.copy(alpha = 0.3f))
                .clickable {
                    showBottomSheet = true
                }
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row {
                Text(
                    text = if (selectedFeatures.isEmpty()) "Select Features" else selectedFeatures.joinToString(
                        ", "
                    ),
                    color = Color.Black,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

            }
        }
        Text(
            text = "${selectedFeatures.size} features added",
            style = MaterialTheme.typography.bodySmall,
        )
    }

    HorizontalDivider(
        thickness = 1.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 10.dp)
    )
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,


            ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxHeight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Vehicle Features",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(featureList) { feature ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    if (selectedFeatures.contains(feature)) {
                                        selectedFeatures.remove(feature)
                                    } else {
                                        selectedFeatures.add(feature)
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedFeatures.contains(feature),
                                onCheckedChange = {
                                    if (selectedFeatures.contains(feature)) {
                                        selectedFeatures.remove(feature)
                                    } else {
                                        selectedFeatures.add(feature)
                                    }
                                }
                            )
                            Text(text = feature, fontSize = 16.sp)
                        }
                    }
                }

                CustomButton(
                    onClick = { showBottomSheet = false },
                    text = "Done"
                )

            }
        }
    }
}

@Composable
fun AddCarDetails() {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column {
            Text(
                text = buildAnnotatedString {
                    append("Ad title")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                        append("*")
                    }
                }, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = title, onValueChange = { title = it }, imeAction = ImeAction.Next
            )
        }

        Column {
            Text(
                text = buildAnnotatedString {
                    append("Description")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                        append("*")
                    }
                }, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = description,
                onValueChange = {
                    if (it.length <= 100) {
                        description = it
                    }
                },
                singleLine = false,
                modifier = Modifier.height(100.dp),
                maxLines = 3,
                imeAction = ImeAction.Next

            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
            ) {
                val wordCount =
                    description.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }.size
                val isValid = wordCount >= 12

                Text(
                    text = if (wordCount < 12) "12 Words minimum" else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isValid) Color.Gray else Color.Red,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${description.length}/100",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier
                )
            }
        }

        Column {
            Text(
                text = buildAnnotatedString {
                    append("Price")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                        append("*")
                    }
                }, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = price,
                onValueChange = { price = it },
                onlyNumbers = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.rupee),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                },
                keyboardType = KeyboardType.Number,
            )
        }

        HorizontalDivider(
            thickness = 1.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}

@Composable
fun ContactDetailSec() {
    var messageChecked by remember { mutableStateOf(true) }
    var phoneChecked by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            Image(
                painter = painterResource(id = R.drawable.right_sign),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
            Text(
                text = buildAnnotatedString {
                    append("Contact Details")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                        append("*")
                    }
                }, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = if (isEdit) "Save Detail" else "Edit",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    isEdit = !isEdit
                }
            )

        }
        if (!isEdit) {
            Row(
                verticalAlignment = Alignment.Top,
            ) {
                Checkbox(
                    checked = messageChecked,
                    onCheckedChange = { messageChecked = it },

                    )
                Column {
                    Text(
                        text = "Messages on platform",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Get notified via")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(" User@gmail.com")

                            }
                        }, style = MaterialTheme.typography.bodyLarge, color = Color.Gray


                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = phoneChecked,
                    onCheckedChange = { phoneChecked = it },

                    )
                Text(
                    text = "Phone:", fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.size(10.dp))
                IconButton(onClick = {
                    isEdit = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "Add phone number", fontSize = 16.sp, color = Color.Gray
                )


            }
        } else {
            Text(
                text = "Messages on platform",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
    HorizontalDivider(
        thickness = 1.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 10.dp)
    )

}

@Composable
fun LocationDetail() {
    var showMap by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            Text(
                text = buildAnnotatedString {
                    append("Location")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                        append("*")
                    }
                }, fontSize = 20.sp, fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Edit",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Delhi, India ")
                }
                append("/ 209801")
            },
            fontSize = 16.sp,

            )
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Checkbox(
                checked = showMap,
                onCheckedChange = { showMap = it },

                )
            Text(
                text = "Show a map on my ad", fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
        }
        if (showMap) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onPrimary)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.map_img),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f),
                        contentScale = ContentScale.Crop

                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Delhi, India",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "Only the approximate area will be shown", color = Color.Gray
                        )
                    }

                }
            }

        }


    }

    HorizontalDivider(
        thickness = 1.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 10.dp)
    )
    CustomButton(
        onClick = {}, text = "Post", modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    )

}

