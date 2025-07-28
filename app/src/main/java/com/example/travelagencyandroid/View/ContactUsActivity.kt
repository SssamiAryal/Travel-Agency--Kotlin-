package com.example.travelagencyandroid.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelagencyandroid.View.ui.theme.TravelAgencyAndroidTheme
import androidx.compose.foundation.shape.RoundedCornerShape

class ContactUsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelAgencyAndroidTheme {
                ContactUsScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Contact Us",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF004D40)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { padding ->
        Card(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "JourneyTrekker",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text("📍 Address: Kathmandu, Nepal", fontSize = 16.sp)
                Text("📞 Phone: +977-9800000000", fontSize = 16.sp)
                Text("📧 Email: journeytrekker@travel.com", fontSize = 16.sp)
                Text("🌐 Website: www.journeytrekker.com", fontSize = 16.sp)
            }
        }
    }
}
