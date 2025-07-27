package com.example.travelagencyandroid.View

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelagencyandroid.R
import com.example.travelagencyandroid.View.ui.theme.TravelAgencyAndroidTheme
import java.util.Calendar

class BookingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val destinationName = intent.getStringExtra("destination_name") ?: "Unknown"
        val destinationDesc = intent.getStringExtra("destination_desc") ?: ""
        val destinationImage = intent.getIntExtra("destination_image", R.drawable.banner)

        setContent {
            TravelAgencyAndroidTheme {
                BookingScreen(
                    name = destinationName,
                    description = destinationDesc,
                    imageRes = destinationImage,
                    onBackClick = { finish() },
                    onBookClick = { fullName, email, phone, goingDate, returnDate, travelers, travelClass ->

                        println("Booking for $fullName, Email: $email, Phone: $phone, From $goingDate to $returnDate, Travelers: $travelers, Class: $travelClass")
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    name: String,
    description: String,
    imageRes: Int,
    onBackClick: () -> Unit,
    onBookClick: (
        fullName: String,
        email: String,
        phone: String,
        goingDate: String,
        returnDate: String,
        travelers: String,
        travelClass: String
    ) -> Unit
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var goingDate by remember { mutableStateOf("") }
    var returnDate by remember { mutableStateOf("") }
    var travelers by remember { mutableStateOf("") }

    val travelClasses = listOf("Economy", "Business", "First Class")
    var expanded by remember { mutableStateOf(false) }
    var selectedTravelClass by remember { mutableStateOf(travelClasses[0]) }

    fun showDatePicker(currentDate: String, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        if (currentDate.isNotBlank()) {
            val parts = currentDate.split("-")
            if (parts.size == 3) {
                calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
            }
        }
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
                title = {
                    Text(
                        text = "Book Your Trip",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF00796B))
            )
        },
        containerColor = Color(0xFFF0F5F5)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = elevatedCardElevation(12.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "$name Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xCC004D40)),
                                    startY = 100f
                                )
                            )
                    )
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    )
                }
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            OutlinedTextField(
                value = travelers,
                onValueChange = { travelers = it.filter { char -> char.isDigit() } },
                label = { Text("Number of Travelers") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = goingDate,
                onValueChange = { goingDate = it },
                label = { Text("Going Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker(goingDate) {
                            goingDate = it
                        }
                    },
                enabled = false,
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        showDatePicker(goingDate) {
                            goingDate = it
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Select Going Date"
                        )
                    }
                }
            )

            OutlinedTextField(
                value = returnDate,
                onValueChange = { returnDate = it },
                label = { Text("Return Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker(returnDate) {
                            returnDate = it
                        }
                    },
                enabled = false,
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        showDatePicker(returnDate) {
                            returnDate = it
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Select Return Date"
                        )
                    }
                }
            )

            Box {
                OutlinedTextField(
                    value = selectedTravelClass,
                    onValueChange = { },
                    label = { Text("Travel Class") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_drop_down), contentDescription = "Dropdown")
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
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

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onBookClick(fullName, email, phone, goingDate, returnDate, travelers, selectedTravelClass)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text(
                    text = "Book Now",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}
