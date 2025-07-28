package com.example.travelagencyandroid.Repository

import com.example.travelagencyandroid.Model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
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

    fun updateBooking(
        booking: Booking,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (booking.bookingId.isBlank()) {
            return onFailure(Exception("Booking ID is required for update"))
        }
        db.collection("Bookings")
            .document(booking.bookingId)
            .set(booking)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteBooking(
        bookingId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (bookingId.isBlank()) {
            return onFailure(Exception("Booking ID is required for deletion"))
        }
        db.collection("Bookings")
            .document(bookingId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    suspend fun getUserBookings(): List<Booking> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = db.collection("Bookings")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            snapshot.documents.map { it.toObject(Booking::class.java)!! }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
