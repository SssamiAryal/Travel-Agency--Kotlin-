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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.travelagencyandroid.View.ui.theme.TravelAgencyAndroidTheme

class AboutUsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelAgencyAndroidTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "About Us",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF004D40)),
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
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                "Welcome to JourneyTrekker!",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "At Journey Trekker, we believe that travel is more than just visiting new places — it's about creating lasting memories, embracing cultures, and discovering yourself along the way. Our mission is to make your journeys smooth, affordable, and unforgettable by connecting you with top destinations and trusted services worldwide.",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Our Vision:\nTo be the most trusted travel partner, inspiring wanderlust and making global exploration accessible to everyone.",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Our Values:\n• Customer Focus: Your satisfaction is our priority.\n• Integrity: Transparent and honest service.\n• Innovation: Constantly improving to enhance your travel experience.\n• Sustainability: Promoting responsible travel to protect our planet.",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Join thousands of happy travelers who trust Journey Trekker for their adventures. Whether you’re dreaming of exotic beaches, cultural cities, or luxury escapes, we’re here to make every trip a journey to remember!",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
