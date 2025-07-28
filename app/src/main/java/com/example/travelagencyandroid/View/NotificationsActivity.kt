package com.example.travelagencyandroid.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelagencyandroid.View.ui.theme.TravelAgencyAndroidTheme
import androidx.compose.ui.text.font.FontWeight

class NotificationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelAgencyAndroidTheme {
                NotificationsScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Notifications",
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
        val notifications = listOf(
            "🌆 New York: Limited offer – 25% off city tours!",
            "🏝️ Maldives: Exclusive beachside packages available now!",
            "🗾 Japan: Spring Sakura Festival bookings open!"
        )

        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(notifications) { note ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = note,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
