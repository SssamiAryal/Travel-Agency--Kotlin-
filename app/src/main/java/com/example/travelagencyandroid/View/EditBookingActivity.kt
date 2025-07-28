package com.example.travelagencyandroid.View

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelagencyandroid.Model.Booking
import com.example.travelagencyandroid.ViewModel.BookingViewModel
import com.example.travelagencyandroid.View.ui.theme.TravelAgencyAndroidTheme
import java.util.*

class EditBookingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val booking = intent.getParcelableExtra<Booking>("booking") ?: Booking()

        setContent {
            TravelAgencyAndroidTheme {
                val viewModel: BookingViewModel = viewModel()
                EditBookingScreen(
                    booking = booking,
                    onBackClick = { finish() },
                    onSaveClick = { updatedBooking ->
                        viewModel.updateBooking(
                            updatedBooking,
                            onSuccess = { finish() },
                            onFailure = { /* TODO: Show error message */ }
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookingScreen(
    booking: Booking,
    onBackClick: () -> Unit,
    onSaveClick: (Booking) -> Unit,
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf(booking.fullName) }
    var email by remember { mutableStateOf(booking.email) }
    var phone by remember { mutableStateOf(booking.phone) }
    var goingDate by remember { mutableStateOf(booking.goingDate) }
    var returnDate by remember { mutableStateOf(booking.returnDate) }
    var travelers by remember { mutableStateOf(booking.travelers) }
    val travelClasses = listOf("Economy", "Business", "First Class")
    var expanded by remember { mutableStateOf(false) }
    var selectedTravelClass by remember { mutableStateOf(booking.travelClass.ifEmpty { travelClasses[0] }) }

    fun showDatePicker(currentDate: String, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formatted = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                onDateSelected(formatted)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Booking", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF00796B))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            OutlinedTextField(
                value = travelers,
                onValueChange = { travelers = it.filter { c -> c.isDigit() } },
                label = { Text("Number of Travelers") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = goingDate,
                onValueChange = {},
                label = { Text("Going Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker(goingDate) { goingDate = it } },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker(goingDate) { goingDate = it } }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Select going date")
                    }
                }
            )

            OutlinedTextField(
                value = returnDate,
                onValueChange = {},
                label = { Text("Return Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker(returnDate) { returnDate = it } },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker(returnDate) { returnDate = it } }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Select return date")
                    }
                }
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedTravelClass,
                    onValueChange = {},
                    label = { Text("Travel Class") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    travelClasses.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedTravelClass = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val updatedBooking = booking.copy(
                        fullName = fullName,
                        email = email,
                        phone = phone,
                        goingDate = goingDate,
                        returnDate = returnDate,
                        travelers = travelers,
                        travelClass = selectedTravelClass
                    )
                    onSaveClick(updatedBooking)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text("Save Changes", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
