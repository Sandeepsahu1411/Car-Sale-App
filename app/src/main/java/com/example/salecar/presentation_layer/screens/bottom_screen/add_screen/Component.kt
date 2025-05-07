package com.example.salecar.presentation_layer.screens.bottom_screen.add_screen

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.salecar.data_layer.response.category_res.Children
import com.example.salecar.data_layer.response.category_res.Data
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
    selectedCategory: MutableState<String?>
) {
    val selectedParentCategory = remember { mutableStateOf<Data?>(null) }
    val subcategories = remember { mutableStateListOf<Children>() }
    var parentExpanded by remember { mutableStateOf(false) }
    var subcategoryExpanded by remember { mutableStateOf(false) }
    var selectedSubCategory by remember { mutableStateOf<Children?>(null) }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                selectedSubCategory = null
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
                        value = selectedSubCategory?.name ?: "Subcategory",
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
                                    selectedSubCategory = sub
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
fun CustomDivider() {
    HorizontalDivider(
        thickness = 1.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 5.dp)
    )
}