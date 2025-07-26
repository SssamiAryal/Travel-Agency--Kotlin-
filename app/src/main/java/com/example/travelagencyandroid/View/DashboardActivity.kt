package com.example.travelagencyandroid.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
                DashboardScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen() {
    val destinations = listOf(
        Triple("Paris", "City of Light – from \$799", R.drawable.paris),
        Triple("Maldives", "Tropical escape – from \$999", R.drawable.maldives),
        Triple("Tokyo", "Culture meets modern – from \$899", R.drawable.tokyo),
        Triple("New York", "The city that never sleeps – from \$849", R.drawable.newyork),
        Triple("Rome", "Historic wonders – from \$699", R.drawable.rome)
    )

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "🌍 JourneyTrekker",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF004D40),
                    scrolledContainerColor = Color(0xFF00251A)
                ),
                modifier = Modifier.shadow(12.dp)
            )
        },
        containerColor = Color(0xFFF0F5F5),
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .shadow(10.dp, RoundedCornerShape(20.dp))
            ) {
                BookTripButton()
            }
        }
    ) { paddingValues ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 80.dp) // to avoid bottom bar overlap
        ) {
            item {
                Text(
                    "Explore the World With Us!",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF00332E),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item { BannerImage() }

            stickyHeader {
                Surface(
                    color = Color(0xFFF0F5F5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "✈ Featured Packages",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00695C),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            item {
                FeaturedPackagesCarousel(destinations)
            }

            stickyHeader {
                Surface(
                    color = Color(0xFFF0F5F5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "🌟 Popular Destinations",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004D40),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            items(destinations) { (title, desc, img) ->
                DestinationCard(title, desc, img)
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
            .padding(horizontal = 16.dp)
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
fun DestinationCard(title: String, description: String, imageRes: Int) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(700)),
        exit = fadeOut(animationSpec = tween(700))
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp)
                .shadow(10.dp, RoundedCornerShape(20.dp)),
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
}

@Composable
fun BookTripButton() {
    Button(
        onClick = { /* TODO: Navigate to booking */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
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

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    TravelAgencyAndroidTheme {
        DashboardScreen()
    }
}
