package com.example.travelagencyandroid.Model

data class Booking(
    val bookingId: String = "",
    val userId: String = "",
    val destinationName: String = "",
    val destinationDescription: String = "",
    val goingDate: String = "",
    val returnDate: String = "",
    val travelers: Int = 1,
    val travelClass: String = ""
)
