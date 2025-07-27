package com.example.travelagencyandroid.Repository

import com.example.travelagencyandroid.Model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class BookingRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun addBooking(
        booking: Booking,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return onFailure(Exception("User not logged in"))
        val bookingId = UUID.randomUUID().toString()

        val newBooking = booking.copy(
            bookingId = bookingId,
            userId = userId
        )

        db.collection("Bookings")
            .document(bookingId)
            .set(newBooking)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}
