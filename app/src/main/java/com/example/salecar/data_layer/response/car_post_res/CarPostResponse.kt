package com.example.salecar.data_layer.response.car_post_res

import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import androidx.core.net.toUri

data class CarPostResponse(
    val message: String,
    val status: String
)

data class CarPostRequest(
    val category_id: String,
    val title: String,
    val description: String,
    val plate_no: String,
    val price: String,
    val email: String,
    val visibility: String,
    val address: String,
    val video_link: String,
    val website_link: String,
    val country: String,
    val latitude: String,
    val longitude: String,
    val year: String,
    val body_type: String,
    val transmission: String,
    val colour: String,
    val seats: String,
    val doors: String,
    val luggage_capacity: String,
    val fuel_type: String,
    val engine_power: String,
    val engine_size: String,
    val top_speed: String,
    val acceleration: String,
    val fuel_consumption: String,
    val fuel_capacity: String,
    val insurance_group: String,
    val images: List<String>,
    val contact_no: String,
    val brochure_engine_size: String,
)
fun CarPostRequest.toMultipart(context: android.content.Context): Pair<Map<String, RequestBody>, List<MultipartBody.Part>> {
    val map = mutableMapOf<String, RequestBody>()

    fun String.toRequestBody() = RequestBody.create("text/plain".toMediaTypeOrNull(), this)

    map["category_id"] = category_id.toRequestBody()
    map["title"] = title.toRequestBody()
    map["description"] = description.toRequestBody()
    map["plate_no"] = plate_no.toRequestBody()
    map["price"] = price.toRequestBody()
    map["email"] = email.toRequestBody()
    map["visibility"] = visibility.toRequestBody()
    map["address"] = address.toRequestBody()
    map["video_link"] = video_link.toRequestBody()
    map["website_link"] = website_link.toRequestBody()
    map["country"] = country.toRequestBody()
    map["latitude"] = latitude.toRequestBody()
    map["longitude"] = longitude.toRequestBody()
    map["year"] = year.toRequestBody()
    map["body_type"] = body_type.toRequestBody()
    map["transmission"] = transmission.toRequestBody()
    map["colour"] = colour.toRequestBody()
    map["seats"] = seats.toRequestBody()
    map["doors"] = doors.toRequestBody()
    map["luggage_capacity"] = luggage_capacity.toRequestBody()
    map["fuel_type"] = fuel_type.toRequestBody()
    map["engine_power"] = engine_power.toRequestBody()
    map["engine_size"] = engine_size.toRequestBody()
    map["top_speed"] = top_speed.toRequestBody()
    map["acceleration"] = acceleration.toRequestBody()
    map["fuel_consumption"] = fuel_consumption.toRequestBody()
    map["fuel_capacity"] = fuel_capacity.toRequestBody()
    map["insurance_group"] = insurance_group.toRequestBody()
    map["contact_no"] = contact_no.toRequestBody()
    map["brochure_engine_size"] = brochure_engine_size.toRequestBody()

    val imageParts = images.mapIndexedNotNull { index, uri ->
        try {
            val inputStream = context.contentResolver.openInputStream(uri.toUri())
            val file = File.createTempFile("img_", ".jpg", context.cacheDir)
            inputStream?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images[]", file.name, reqFile)
        } catch (e: Exception) {
            null
        }
    }

    return Pair(map, imageParts)
}
