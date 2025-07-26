package com.example.travelagencyandroid.View

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.travelagencyandroid.View.ui.theme.TravelAgencyAndroidTheme
import com.example.travelagencyandroid.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelAgencyAndroidTheme {
                Scaffold { innerPadding ->
                    RegisterScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(innerPaddingValues: PaddingValues) {
    val context = LocalContext.current
    val auth = Firebase.auth

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var countryExpanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("Select Country") }
    val countryOptions = listOf("Nepal", "India", "China")
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.plane),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .padding(innerPaddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .padding(top = 150.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = { Text("First Name") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = { Text("Last Name") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedCountry,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            textFieldSize = it.size.toSize()
                        }
                        .clickable { countryExpanded = true },
                    placeholder = { Text("Select Country") },
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledIndicatorColor = androidx.compose.ui.graphics.Color.Gray,
                        disabledContainerColor = androidx.compose.ui.graphics.Color.White
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                )

                DropdownMenu(
                    expanded = countryExpanded,
                    onDismissRequest = { countryExpanded = false },
                    modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    countryOptions.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country) },
                            onClick = {
                                selectedCountry = country
                                countryExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (email.isNotEmpty() && password.length >= 6) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                                    // After registration, navigate to Login screen:
                                    context.startActivity(Intent(context, LoginActivity::class.java))
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Registration Failed: ${task.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Enter valid email and password (6+ chars)", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    TravelAgencyAndroidTheme {
        RegisterScreen(PaddingValues(0.dp))
    }
}

