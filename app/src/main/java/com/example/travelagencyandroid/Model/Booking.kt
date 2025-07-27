package com.example.travelagencyandroid.Model

data class Booking(
    val bookingId: String = "",
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val goingDate: String = "",
    val returnDate: String = "",
    val travelers: String = "",
    val travelClass: String = ""
)
