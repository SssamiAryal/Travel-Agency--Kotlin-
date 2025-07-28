package com.example.travelagencyandroid.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelagencyandroid.Model.Booking
import com.example.travelagencyandroid.Repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    private val repository = BookingRepository()

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings

    fun loadUserBookings() {
        viewModelScope.launch {
            val userBookings = repository.getUserBookings()
            _bookings.value = userBookings
        }
    }

    fun submitBooking(
        booking: Booking,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.addBooking(booking, {
            loadUserBookings()
            onSuccess()
        }, onFailure)
    }

    fun updateBooking(
        booking: Booking,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.updateBooking(booking, {
            loadUserBookings()
            onSuccess()
        }, onFailure)
    }

    fun deleteBooking(
        bookingId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.deleteBooking(bookingId, {
            loadUserBookings()
            onSuccess()
        }, onFailure)
    }
}
