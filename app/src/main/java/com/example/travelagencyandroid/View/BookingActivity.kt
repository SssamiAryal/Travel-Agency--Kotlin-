package com.example.travelagencyandroid.View

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.util.*

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
                        println("Booking: $fullName | $email | $phone | $goingDate to $returnDate | $travelers | $travelClass")
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
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val formatted = "%04d-%02d-%02d".format(year, month + 1, day)
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
                    Text("Book Your Trip", color = Color.White, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(12.dp)
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
                                    listOf(Color.Transparent, Color(0xCC004D40)),
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

            Text(text = description, style = MaterialTheme.typography.bodyLarge)

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
                label = { Text("Phone Number") },
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
                onValueChange = { },
                label = { Text("Going Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker(goingDate) { goingDate = it } },
                enabled = false,
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker(goingDate) { goingDate = it } }) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                    }
                }
            )

            OutlinedTextField(
                value = returnDate,
                onValueChange = { },
                label = { Text("Return Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker(returnDate) { returnDate = it } },
                enabled = false,
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker(returnDate) { returnDate = it } }) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                    }
                }
            )

            Box {
                OutlinedTextField(
                    value = selectedTravelClass,
                    onValueChange = {},
                    label = { Text("Travel Class") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_drop_down), contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
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
                    onBookClick(fullName, email, phone, goingDate, returnDate, travelers, selectedTravelClass)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text("Book Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }
}
