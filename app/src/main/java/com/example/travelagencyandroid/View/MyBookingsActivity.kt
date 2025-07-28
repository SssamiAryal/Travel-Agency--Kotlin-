package com.example.travelagencyandroid.View

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelagencyandroid.Model.Booking
import com.example.travelagencyandroid.ViewModel.BookingViewModel
import com.example.travelagencyandroid.View.ui.theme.TravelAgencyAndroidTheme

class MyBookingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TravelAgencyAndroidTheme {
                MyBookingsScreen(
                    onBackClick = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    onBackClick: () -> Unit,
    bookingViewModel: BookingViewModel = viewModel()
) {
    val bookings by bookingViewModel.bookings.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        bookingViewModel.loadUserBookings()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "My Bookings",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF00796B)
                )
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (bookings.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "You have no bookings yet.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(bookings) { booking ->
                            BookingCard(
                                booking = booking,
                                onEditClick = {
                                    val intent = Intent(context, EditBookingActivity::class.java)
                                    intent.putExtra("booking", booking)
                                    context.startActivity(intent)
                                },
                                onDeleteClick = {
                                    bookingViewModel.deleteBooking(
                                        booking.bookingId,
                                        onSuccess = { bookingViewModel.loadUserBookings() },
                                        onFailure = { /* TODO: Show error message */ }
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BookingCard(
    booking: Booking,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Destination: ${booking.destinationName}", fontWeight = FontWeight.Bold)
            Text("Name: ${booking.fullName}")
            Text("Email: ${booking.email}")
            Text("Phone: ${booking.phone}")
            Text("Going: ${booking.goingDate} | Return: ${booking.returnDate}")
            Text("Travelers: ${booking.travelers} | Class: ${booking.travelClass}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onEditClick) {
                    Text("Edit")
                }
                TextButton(onClick = onDeleteClick) {
                    Text("Delete", color = Color.Red)
                }
            }
        }
    }
}
