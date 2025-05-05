package com.example.salecar.data_layer.response.car_post_res

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
fun CarPostRequest.toFieldMap(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()

    map["category_id"] = category_id
    map["title"] = title
    map["description"] = description
    map["plate_no"] = plate_no
    map["price"] = price
    map["email"] = email
    map["visibility"] = visibility
    map["address"] = address
    map["video_link"] = video_link
    map["website_link"] = website_link
    map["country"] = country
    map["latitude"] = latitude
    map["longitude"] = longitude
    map["year"] = year
    map["body_type"] = body_type
    map["transmission"] = transmission
    map["colour"] = colour
    map["seats"] = seats
    map["doors"] = doors
    map["luggage_capacity"] = luggage_capacity
    map["fuel_type"] = fuel_type
    map["engine_power"] = engine_power
    map["engine_size"] = engine_size
    map["top_speed"] = top_speed
    map["acceleration"] = acceleration
    map["fuel_consumption"] = fuel_consumption
    map["fuel_capacity"] = fuel_capacity
    map["insurance_group"] = insurance_group
    map["contact_no"] = contact_no
    map["brochure_engine_size"] = brochure_engine_size

    images.forEachIndexed { index, image ->
        map["images[$index]"] = image
    }

    return map
}
