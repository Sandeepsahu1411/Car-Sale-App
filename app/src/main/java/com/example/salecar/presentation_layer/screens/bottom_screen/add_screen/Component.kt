package com.example.salecar.presentation_layer.screens.bottom_screen.add_screen

import android.R.attr.onClick
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
//        subcategories.clear()
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
                    value = selectedParentCategory.value?.name ?: "",
                    onValueChange = {},
                    isEditable = false,
                    placeholderText = "Category",
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
                        value = selectedSubCategory.value?.name ?: "",
                        onValueChange = {},
                        isEditable = false,
                        placeholderText = "Subcategory",
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

                TwoFieldRow(
                    label1 = "Body Type",
                    value1 = vehicleSpec.value.body_type,
                    onValueChange1 = { vehicleSpec.value = vehicleSpec.value.copy(body_type = it) },
                    label2 = "Transmission",
                    value2 = vehicleSpec.value.transmission,
                    onValueChange2 = { vehicleSpec.value = vehicleSpec.value.copy(transmission = it) }
                )

                TwoFieldRow(
                    label1 = "Colour",
                    value1 = vehicleSpec.value.colour,
                    onValueChange1 = { vehicleSpec.value = vehicleSpec.value.copy(colour = it) },
                    label2 = "Seats",
                    value2 = vehicleSpec.value.seats,
                    onValueChange2 = { vehicleSpec.value = vehicleSpec.value.copy(seats = it) },
                    onlyNumbers2 = true
                )

                TwoFieldRow(
                    label1 = "Doors",
                    value1 = vehicleSpec.value.doors,
                    onValueChange1 = { vehicleSpec.value = vehicleSpec.value.copy(doors = it) },
                    label2 = "Luggage Capacity",
                    value2 = vehicleSpec.value.luggage_capacity,
                    onValueChange2 = {
                        vehicleSpec.value = vehicleSpec.value.copy(luggage_capacity = it)
                    },
                    onlyNumbers2 = true


                )

                TwoFieldRow(
                    label1 = "Fuel Type",
                    value1 = vehicleSpec.value.fuel_type,
                    onValueChange1 = { vehicleSpec.value = vehicleSpec.value.copy(fuel_type = it) },
                    label2 = "Engine Power",
                    value2 = vehicleSpec.value.engine_power,
                    onValueChange2 = {
                        vehicleSpec.value = vehicleSpec.value.copy(engine_power = it)
                    },

                    )

                TwoFieldRow(
                    label1 = "Engine Size",
                    value1 = vehicleSpec.value.engine_size,
                    onValueChange1 = {
                        vehicleSpec.value = vehicleSpec.value.copy(engine_size = it)
                    },
                    label2 = "Top Speed",
                    value2 = vehicleSpec.value.top_speed,
                    onValueChange2 = { vehicleSpec.value = vehicleSpec.value.copy(top_speed = it) }
                )

                TwoFieldRow(
                    label1 = "Acceleration",
                    value1 = vehicleSpec.value.acceleration,
                    onValueChange1 = {
                        vehicleSpec.value = vehicleSpec.value.copy(acceleration = it)
                    },
                    label2 = "Fuel Consumption",
                    value2 = vehicleSpec.value.fuel_consumption,
                    onValueChange2 = {
                        vehicleSpec.value = vehicleSpec.value.copy(fuel_consumption = it)
                    }
                )


                TwoFieldRow(
                    label1 = "Fuel Capacity",
                    value1 = vehicleSpec.value.fuel_capacity,
                    onValueChange1 = { vehicleSpec.value = vehicleSpec.value.copy(fuel_capacity = it) },
                    label2 = "Insurance Group",
                    value2 = vehicleSpec.value.insurance_group,
                    onValueChange2 = { vehicleSpec.value = vehicleSpec.value.copy(insurance_group = it) }
                )

                CustomButton(onClick = {
                    showBottomSheet = false
                }, text = "Done")
            }
        }
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onlyNumbers: Boolean = false
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(2.dp))
        CustomTextField(
            value = value,
            onValueChange = onValueChange,
            placeholderText = label,
            modifier = Modifier.fillMaxWidth(),
            onlyNumbers = onlyNumbers
        )
    }
}
@Composable
fun TwoFieldRow(
    label1: String,
    value1: String,
    onValueChange1: (String) -> Unit,
    label2: String,
    value2: String,
    onValueChange2: (String) -> Unit,
    onlyNumbers1: Boolean = false,
    onlyNumbers2: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LabeledTextField(
            label = label1,
            value = value1,
            onValueChange = onValueChange1,
            onlyNumbers = onlyNumbers1,
            modifier = Modifier.weight(1f)
        )
        LabeledTextField(
            label = label2,
            value = value2,
            onValueChange = onValueChange2,
            onlyNumbers = onlyNumbers2,
            modifier = Modifier.weight(1f)
        )
    }
}

