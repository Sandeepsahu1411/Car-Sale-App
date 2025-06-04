package com.example.salecar.presentation_layer.screens.bottom_screen.add_screen

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salecar.R
import com.example.salecar.data_layer.response.category_res.Children
import com.example.salecar.data_layer.response.category_res.Data
import com.example.salecar.presentation_layer.screens.common_component.CustomButton
import com.example.salecar.presentation_layer.screens.common_component.CustomTextField
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import java.util.Locale
import kotlin.collections.forEach


@Composable
fun GoogleMapScreen(
    targetLocation: LatLng?,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(targetLocation ?: LatLng(0.0, 0.0), 15f)
    }
    val markerState = rememberMarkerState(position = targetLocation ?: LatLng(0.0, 0.0))

    val singapore = LatLng(1.35, 103.87)
    val singaporeMarkerState = rememberMarkerState(position = singapore)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(singapore, 10f)
//    }
    LaunchedEffect(targetLocation) {
        targetLocation?.let {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f))
            markerState.position = it
        }
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState, onMapClick = {
            singaporeMarkerState.position = it
        }) {
        Marker(
            state = markerState, title = "Singapore", snippet = "Marker in Singapore"
        )
    }

}

fun getLocationFromPinCode(context: Context, pinCode: String): Pair<LatLng?, String?> {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocationName(pinCode, 1)
        if (!addresses.isNullOrEmpty()) {
            val location = addresses[0]
            val latLng = LatLng(location.latitude, location.longitude)
            val addressLine = location.getAddressLine(0) ?: "Unknown"
            Pair(latLng, addressLine)
        } else Pair(null, null)
    } catch (e: Exception) {
        Pair(null, null)
    }
}


@Composable
fun PinCodeDialog(
    onPinCodeSubmit: (String) -> Boolean,
    onDismiss: () -> Unit,
    pinCode: String,
    onPinCodeChange: (String) -> Unit
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Enter PinCode") }, text = {
        Column {
            TextField(
                value = pinCode,
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) onPinCodeChange(it)
                    errorMessage = null
                },
                label = { Text("PinCode") },
                isError = errorMessage != null,
            )
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }, confirmButton = {
        Button(onClick = {
            val isValid = onPinCodeSubmit(pinCode)
            if (!isValid) {
                errorMessage = "Please enter a valid pin code"
            }
        }) {
            Text("Submit")
        }
    }, dismissButton = {
        Button(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownMenu(
    categoryData: List<Data>,
    selectedCategory: MutableState<String?>,
    selectedParentCategory: MutableState<Data?>,
    selectedSubCategory: MutableState<Children?>
) {
    val subcategories = remember { mutableStateListOf<Children>() }
    var parentExpanded by remember { mutableStateOf(false) }
    var subcategoryExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedParentCategory.value) {
        subcategories.clear()
        selectedParentCategory.value?.let {
            subcategories.addAll(it.children)
        }
    }


    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(bottom = 5.dp)
    ) {
        Column(modifier = Modifier.weight(0.5f)) {
            Text(text = "Select Category", fontWeight = FontWeight.SemiBold)

            ExposedDropdownMenuBox(
                expanded = parentExpanded,
                onExpandedChange = { parentExpanded = !parentExpanded }
            ) {
                CustomTextField(
                    value = selectedParentCategory.value?.name ?: "Category",
                    onValueChange = {},
                    isEditable = false,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = parentExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = parentExpanded,
                    onDismissRequest = { parentExpanded = false }
                ) {
                    categoryData.forEach { parent ->
                        DropdownMenuItem(
                            text = { Text(parent.name) },
                            onClick = {
                                selectedParentCategory.value = parent
                                subcategories.clear()
                                subcategories.addAll(parent.children)
                                selectedSubCategory.value = null
                                selectedCategory.value = null
                                parentExpanded = false
                            }
                        )
                    }
                }
            }
        }
        Column(modifier = Modifier.weight(0.5f)) {
            if (subcategories.isNotEmpty()) {
                Text(text = "Select Subcategory", fontWeight = FontWeight.SemiBold)

                ExposedDropdownMenuBox(
                    expanded = subcategoryExpanded,
                    onExpandedChange = { subcategoryExpanded = !subcategoryExpanded }
                ) {
                    CustomTextField(
                        value = selectedSubCategory.value?.name ?: "Subcategory",
                        onValueChange = {},
                        isEditable = false,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = subcategoryExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = subcategoryExpanded,
                        onDismissRequest = { subcategoryExpanded = false }
                    ) {
                        subcategories.forEach { sub ->
                            DropdownMenuItem(
                                text = { Text(sub.name) },
                                onClick = {
                                    selectedSubCategory.value = sub
                                    selectedCategory.value = sub.id.toString()
                                    subcategoryExpanded = false
                                    Log.d(
                                        "CategorySelection",
                                        "Selected Subcategory ID: ${sub.id}"
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    CustomDivider()

}

@Composable
fun CustomDivider(
    thickness: Dp = 1.dp,
) {
    HorizontalDivider(
        thickness = thickness, color = Color.LightGray, modifier = Modifier.padding(vertical = 5.dp)
    )
}

////   VehicleSpecificationSec

data class VehicleSpecFields(
    var body_type: String = "",
    var transmission: String = "",
    var colour: String = "",
    var seats: String = "",
    var doors: String = "",
    var luggage_capacity: String = "",
    var fuel_type: String = "",
    var engine_power: String = "",
    var engine_size: String = "",
    var top_speed: String = "",
    var acceleration: String = "",
    var fuel_consumption: String = "",
    var fuel_capacity: String = "",
    var insurance_group: String = ""
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSpecificationSec(
    plateNo: String,
    onValueChange: (String) -> Unit,
    vehicleSpec: MutableState<VehicleSpecFields>
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedFeatures = remember { mutableStateListOf<String>() }


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
                Text("Vehicle Specifications", modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
            }
        }

    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Enter Vehicle Specifications", fontWeight = FontWeight.Bold)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        value = vehicleSpec.value.body_type,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(body_type = it)
                        },
                        placeholderText = "Body Type",
                        modifier = Modifier.weight(1f)
                    )
                    CustomTextField(
                        value = vehicleSpec.value.transmission,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(transmission = it)
                        },
                        placeholderText = "Transmission",
                        modifier = Modifier.weight(1f)

                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        value = vehicleSpec.value.colour,
                        onValueChange = { vehicleSpec.value = vehicleSpec.value.copy(colour = it) },
                        placeholderText = "Colour",
                        modifier = Modifier.weight(1f)

                    )
                    CustomTextField(
                        value = vehicleSpec.value.seats,
                        onValueChange = { vehicleSpec.value = vehicleSpec.value.copy(seats = it) },
                        placeholderText = "Seats",
                        modifier = Modifier.weight(1f),
                        onlyNumbers = true

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        value = vehicleSpec.value.doors,
                        onValueChange = { vehicleSpec.value = vehicleSpec.value.copy(doors = it) },
                        placeholderText = "Doors",
                        modifier = Modifier.weight(1f),
                        onlyNumbers = true


                    )
                    CustomTextField(
                        value = vehicleSpec.value.luggage_capacity,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(luggage_capacity = it)
                        },
                        placeholderText = "Luggage Capacity",
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        value = vehicleSpec.value.fuel_type,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(fuel_type = it)
                        },
                        placeholderText = "Fuel Type",
                        modifier = Modifier.weight(1f)

                    )
                    CustomTextField(
                        value = vehicleSpec.value.engine_power,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(engine_power = it)
                        },
                        placeholderText = "Engine Power",
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        value = vehicleSpec.value.engine_size,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(engine_size = it)
                        },
                        placeholderText = "Engine Size",
                        modifier = Modifier.weight(1f)

                    )
                    CustomTextField(
                        value = vehicleSpec.value.top_speed,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(top_speed = it)
                        },
                        placeholderText = "Top Speed",
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        value = vehicleSpec.value.acceleration,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(acceleration = it)
                        },
                        placeholderText = "Acceleration",
                        modifier = Modifier.weight(1f)

                    )
                    CustomTextField(
                        value = vehicleSpec.value.fuel_consumption,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(fuel_consumption = it)
                        },
                        placeholderText = "Fuel Consumption",
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomTextField(
                        value = vehicleSpec.value.fuel_capacity,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(fuel_capacity = it)
                        },
                        placeholderText = "Fuel Capacity",
                        modifier = Modifier.weight(1f)

                    )
                    CustomTextField(
                        value = vehicleSpec.value.insurance_group,
                        onValueChange = {
                            vehicleSpec.value = vehicleSpec.value.copy(insurance_group = it)
                        },
                        placeholderText = "Insurance Group",
                        modifier = Modifier.weight(1f)

                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                CustomButton(onClick = {
                    showBottomSheet = false
                }, text = "Done")
            }
        }
    }
}
