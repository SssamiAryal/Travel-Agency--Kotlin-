package com.example.travelagencyandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelagencyandroid.Model.Booking
import com.example.travelagencyandroid.Repository.BookingRepository

class BookingViewModel : ViewModel() {
    private val repository = BookingRepository()

    fun submitBooking(
        booking: Booking,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.addBooking(booking, onSuccess, onFailure)
    }
}
