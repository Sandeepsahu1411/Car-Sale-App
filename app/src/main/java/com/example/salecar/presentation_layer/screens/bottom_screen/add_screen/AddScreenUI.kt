package com.example.salecar.presentation_layer.screens.bottom_screen.add_screen

import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.salecar.R
import com.example.salecar.data_layer.response.car_post_res.CarPostRequest
import com.example.salecar.data_layer.response.category_res.Children
import com.example.salecar.presentation_layer.navigation.Routes
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomLoadingBar
import com.example.salecar.presentation_layer.screens.common_component.CustomTextField
import com.example.salecar.presentation_layer.view_model.AppViewModel
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreenUI(navController: NavController, viewModel: AppViewModel = hiltViewModel()) {
    val postState = viewModel.carPostState.collectAsState()

    val showDialog = remember { mutableStateOf(true) }
    val context = LocalContext.current

    val sellerType = remember { mutableStateOf<String?>(null) }
    val imageUris = remember { mutableStateListOf<Uri>() }
    var plateNo by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val selectedLocation = remember { mutableStateOf<LatLng?>(null) }
    val addressText = remember { mutableStateOf("Address not found") }
    var pinCode by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val categoryState = viewModel.carCategoryState.collectAsState()
    val subcategories = remember { mutableStateListOf<Children>() }
    val selectedCategory = remember { mutableStateOf<String?>(null) }


    when {
        categoryState.value.loading -> {
            CustomLoadingBar()
        }

        categoryState.value.error != null -> {
            Toast.makeText(context, categoryState.value.error, Toast.LENGTH_SHORT).show()
        }

        categoryState.value.data != null -> {
//            selectedCategory.value = categoryState.value.data?.body()?.data?.get(0)?.id.toString()
            Log.d("CategoryData", "Category Data: ${categoryState.value.data?.body()?.data}")


        }
    }
    when {
        postState.value.loading -> {
            CustomLoadingBar()
        }

        postState.value.error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f), contentAlignment = Alignment.Center
            ) {
                Text(text = postState.value.error ?: "")
            }

            Toast.makeText(context, postState.value.error, Toast.LENGTH_SHORT).show()
        }

        postState.value.data != null -> {
            Toast.makeText(context, "Post Success", Toast.LENGTH_SHORT).show()
            postState.value.data = null
            navController.navigate(Routes.HomeScreenRoute)
        }

    }

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
                .padding(innerPadding)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                categoryState.value.data?.body()?.data?.let { categoryData ->
                    CategoryDropdownMenu(
                        categoryData = categoryData,
                        selectedCategory = selectedCategory
                    )
                }
            }

            item {
                if (selectedCategory.value != null) {
                    Text(
                        text = "Seller Type",
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = sellerType.value == "Private",
                            onClick = { sellerType.value = "Private" }
                        )
                        Text("Private", modifier = Modifier.padding(start = 8.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = sellerType.value == "Trade",
                            onClick = { sellerType.value = "Trade" }
                        )
                        Text("Trade", modifier = Modifier.padding(start = 8.dp))
                    }
                    CustomDivider()

                }
            }


            if (selectedCategory.value != null && sellerType.value != null) {
                item {
                    AddPhotosSec(imageUris)
                }
                item {
                    VehicleSpecificationSec(plateNo, onValueChange = { plateNo = it })
                }
                item {
                    AddCarDetails(
                        title = title,
                        onTitleChange = { title = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        price = price,
                        onPriceChange = { price = it })
                }
                item {
                    ContactDetailSec(
                        email = email,
                        onEmailChange = { email = it },
                        phoneNumber = phoneNumber,
                        onPhoneNumberChange = { phoneNumber = it }
                    )
                }
                item {
                    LocationDetail(selectedLocation, addressText.value, showDialog)
                }
                item {
                    CustomButton(
                        onClick = {
                            viewModel.carPost(
                                CarPostRequest(
                                    category_id = selectedCategory.value ?: "0",
                                    title = title,
                                    description = description,
                                    plate_no = plateNo,
                                    price = price,
                                    email = email,
                                    visibility = "public",
                                    address = addressText.value,
                                    video_link = "",
                                    website_link = "",
                                    country = "India",
                                    latitude = selectedLocation.value?.latitude.toString(),
                                    longitude = selectedLocation.value?.longitude.toString(),
                                    year = "",
                                    body_type = "",
                                    transmission = "",
                                    colour = "",
                                    seats = "",
                                    doors = "",
                                    luggage_capacity = "",
                                    fuel_type = "",
                                    engine_power = "",
                                    engine_size = "",
                                    top_speed = "",
                                    acceleration = "",
                                    fuel_consumption = "",
                                    fuel_capacity = "",
                                    insurance_group = "",
                                    images = imageUris.map { it.toString() },
                                    contact_no = phoneNumber,
                                    brochure_engine_size = ""
                                ),
                                context
                            )
                        }, text = "Post", modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                }
            }
        }
        if (selectedCategory.value != null && sellerType.value != null) {
            if (showDialog.value) {
                PinCodeDialog(
                    onPinCodeSubmit = { pinCode ->
                        val (location, address) = getLocationFromPinCode(context, pinCode)
                        return@PinCodeDialog if (location != null && address != null) {
                            selectedLocation.value = location
                            addressText.value = address
                            showDialog.value = false
                            true
                        } else {
                            false
                        }
                    },
                    onDismiss = { showDialog.value = false },
                    pinCode = pinCode,
                    onPinCodeChange = { pinCode = it })

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhotosSec(imageUris: MutableList<Uri>) {

    val maxImages = 20
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { selectedUris ->
        val newImages = selectedUris.take(maxImages - imageUris.size)
        imageUris.addAll(newImages)
    }

    Column(
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

                            Icon(
                                imageVector = Icons.Default.Close,
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
    CustomDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSpecificationSec(plateNo: String, onValueChange: (String) -> Unit) {

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
            CustomTextField(
                value = plateNo,
                onValueChange = { onValueChange(it.uppercase()) },
                placeholderText = "Enter REG",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.car_number_plate),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                })
        }
//        CustomButton(
//            onClick = { }, text = "Look up details", enabled = plateNo.isNotBlank()
//        )
//        Spacer(modifier = Modifier.size(5.dp))
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
                .padding(horizontal = 15.dp), contentAlignment = Alignment.CenterStart) {
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
                    text = "Vehicle Features", fontSize = 20.sp, fontWeight = FontWeight.Bold
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
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedFeatures.contains(feature), onCheckedChange = {
                                    if (selectedFeatures.contains(feature)) {
                                        selectedFeatures.remove(feature)
                                    } else {
                                        selectedFeatures.add(feature)
                                    }
                                })
                            Text(text = feature, fontSize = 16.sp)
                        }
                    }
                }

                CustomButton(
                    onClick = { showBottomSheet = false }, text = "Done"
                )

            }
        }
    }
}

@Composable
fun AddCarDetails(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit
) {


    Column(
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
                value = title, onValueChange = { onTitleChange(it) }, imeAction = ImeAction.Next
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
                        onDescriptionChange(it)
                    }
                },
                singleLine = false,
                modifier = Modifier.height(100.dp),
                maxLines = 3,
                imeAction = ImeAction.Next

            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 5.dp),
//            ) {
//                val wordCount =
//                    description.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }.size
//                val isValid = wordCount >= 12
//
//                Text(
//                    text = if (wordCount < 12) "12 Words minimum" else "",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = if (isValid) Color.Gray else Color.Red,
//                    modifier = Modifier.weight(1f)
//                )
            Text(
                text = "${description.length}/100",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
//            }
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
                onValueChange = { onPriceChange(it) },
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
        CustomDivider()
    }
}

@Composable
fun ContactDetailSec(
    email: String,
    onEmailChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit
) {
    var messageChecked by remember { mutableStateOf(true) }
    var phoneChecked by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }

    Column(
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
                })

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
                            append("Get notified via ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(if (email != "") email else "User@gmail.com")

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
                if (phoneNumber != "") {
                    Text(
                        text = phoneNumber, fontSize = 16.sp, color = Color.Gray
                    )
                } else {
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

            }
        } else {
            Text(text = "Email :", fontWeight = FontWeight.Bold)
            CustomTextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                placeholderText = "Enter Email",
            )
            Text(text = "Phone Number :", fontWeight = FontWeight.Bold)
            CustomTextField(
                value = phoneNumber,
                onValueChange = { onPhoneNumberChange(it) },
                placeholderText = "Enter Phone Number",
                keyboardType = KeyboardType.Phone,
                onlyNumbers = true,
            )
        }
    }
    CustomDivider()

}

@Composable
fun LocationDetail(
    selectedLocation: MutableState<LatLng?>, address: String, showDialog: MutableState<Boolean>
) {
    var showMap by rememberSaveable { mutableStateOf(false) }
    Column{
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
            IconButton(onClick = {
                showDialog.value = true
            }) {
                Text(
                    text = "Edit",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Text(
            text = address, fontSize = 16.sp, fontWeight = FontWeight.Bold
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f),
                        contentAlignment = Alignment.Center
                    ) {
                        GoogleMapScreen(
                            targetLocation = selectedLocation.value,
                        )

                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = address,
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
    CustomDivider()

}













