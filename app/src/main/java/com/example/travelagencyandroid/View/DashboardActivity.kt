package com.example.travelagencyandroid.View

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelagencyandroid.R
import com.example.travelagencyandroid.View.ui.theme.TravelAgencyAndroidTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelAgencyAndroidTheme {
                DashboardScreen(
                    onLogout = {
                        finish()
                        startActivity(Intent(this, LoginActivity::class.java))
                    },
                    onMyBookingsClick = {
                        startActivity(Intent(this, MyBookingsActivity::class.java))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onLogout: () -> Unit, onMyBookingsClick: () -> Unit) {
    val context = LocalContext.current

    val destinations = listOf(
        Triple("Paris", "City of Light – from \$799", R.drawable.paris),
        Triple("Maldives", "Tropical escape – from \$999", R.drawable.maldives),
        Triple("Tokyo", "Culture meets modern – from \$899", R.drawable.tokyo),
        Triple("New York", "The city that never sleeps – from \$849", R.drawable.newyork),
        Triple("Rome", "Historic wonders – from \$699", R.drawable.rome)
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(24.dp))
                Text(
                    "Menu",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                NavigationDrawerItem(label = { Text("Home") }, selected = false, onClick = {})
                NavigationDrawerItem(label = { Text("My Bookings") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }
                    onMyBookingsClick()
                })
                NavigationDrawerItem(label = { Text("Notifications") }, selected = false, onClick = {})
                NavigationDrawerItem(label = { Text("Contact Us") }, selected = false, onClick = {})
                NavigationDrawerItem(label = { Text("Profile") }, selected = false, onClick = {})
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "🌍 JourneyTrekker",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Image(
                                painter = painterResource(R.drawable.ic_profile),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold)
                                },
                                onClick = {
                                    expanded = false
                                    onLogout()
                                }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF004D40))
                )
            },
            containerColor = Color(0xFFF0F5F5)
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Text(
                        "Explore the World With Us!",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF00332E)
                    )
                }
                item { BannerImage() }
                item {
                    Text(
                        "✈ Featured Packages",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00695C),
                    )
                }
                item { FeaturedPackagesCarousel(destinations) }
                item {
                    Text(
                        "🌟 Popular Destinations",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004D40)
                    )
                }
                itemsIndexed(destinations) { index, (title, desc, img) ->
                    DestinationCard(
                        title = title,
                        description = desc,
                        imageRes = img,
                        isSelected = (index == selectedIndex),
                        onClick = {
                            selectedIndex = index
                        }
                    )
                }
                item {
                    BookTripButton {
                        val (title, desc, img) = destinations[selectedIndex]
                        val intent = Intent(context, BookingActivity::class.java).apply {
                            putExtra("destination_name", title)
                            putExtra("destination_desc", desc)
                            putExtra("destination_image", img)
                        }
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun BannerImage() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .shadow(15.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        elevation = elevatedCardElevation(15.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "Travel banner",
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun FeaturedPackagesCarousel(destinations: List<Triple<String, String, Int>>) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(currentIndex) {
        delay(4000)
        currentIndex = (currentIndex + 1) % destinations.size
        coroutineScope.launch {
            listState.animateScrollToItem(currentIndex)
        }
    }

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(destinations) { index, (title, desc, img) ->
            val isSelected = index == currentIndex
            FeaturedPackageCard(
                title = title,
                description = desc,
                imageRes = img,
                scale = if (isSelected) 1.05f else 0.9f,
                shadow = if (isSelected) 16.dp else 6.dp
            )
        }
    }
}

@Composable
fun FeaturedPackageCard(
    title: String,
    description: String,
    imageRes: Int,
    scale: Float,
    shadow: Dp
) {
    val scaleAnim by animateFloatAsState(targetValue = scale, animationSpec = tween(600))
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp)
            .graphicsLayer(scaleX = scaleAnim, scaleY = scaleAnim)
            .shadow(shadow, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        elevation = elevatedCardElevation(shadow)
    ) {
        Box {
            Image(
                painter = painterResource(imageRes),
                contentDescription = "$title image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xCC004D40)),
                            startY = 80f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text(description, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun DestinationCard(
    title: String,
    description: String,
    imageRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF00796B) else Color.Transparent
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .shadow(10.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .background(Color.White),
        border = BorderStroke(4.dp, borderColor),
        elevation = elevatedCardElevation(10.dp)
    ) {
        Box {
            Image(
                painter = painterResource(imageRes),
                contentDescription = "$title Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xDD000000)),
                            startY = 150f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(description, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun BookTripButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
    ) {
        Text(
            text = "Book Your Trip Now",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}
