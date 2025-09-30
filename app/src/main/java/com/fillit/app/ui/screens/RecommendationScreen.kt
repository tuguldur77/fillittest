/*@file:OptIn(ExperimentalMaterial3Api::class)

package com.fillit.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

data class Place(
    val id: String,
    val name: String,
    val category: String,
    val rating: Double,
    val distance: String,
    val priceRange: String,
    val tags: List<String> = emptyList()
)

@Composable
fun RecommendationScreen(onBack: () -> Unit, onOpenPlace: (String) -> Unit) {
    val places = remember {
        listOf(
            Place("1", "성수동 카페", "카페", 4.8, "320m", "₩₩", listOf("조용한", "WiFi", "디저트")),
            Place("2", "성수동 갤러리", "전시", 4.7, "450m", "₩", listOf("전시", "포토"))
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("추천") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { inner ->
        LazyColumn(Modifier.padding(inner)) {
            items(places) { p ->
                ElevatedCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    onClick = { onOpenPlace(p.id) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(p.name, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${p.category} · ${p.distance} · ${p.priceRange}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Row(Modifier.padding(top = 8.dp)) {
                            p.tags.forEach { t ->
                                AssistChip(onClick = {}, label = { Text(t) })
                                Spacer(Modifier.width(6.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}*/

@file:OptIn(ExperimentalMaterial3Api::class)

package com.fillit.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fillit.app.navigation.Route
import com.fillit.app.ui.components.FillItBottomBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecommendationScreen(
    current: Route,
    onBack: () -> Unit,
    onFilter: () -> Unit,
    onNavigate: (Route) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF3F4F6), Color.White)
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "빈 시간 추천",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF111827)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = onFilter) {
                            Icon(Icons.Default.Edit, contentDescription = "Filter")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            bottomBar = {
                FillItBottomBar(current = current, onNavigate = onNavigate)
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .padding(inner)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==== Selected Filters Row ====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(50))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(Icons.Default.DateRange, "14:30 ~ 16:00")
                    FilterChip(Icons.Default.LocationOn, "성수동 근처")
                }

                // ==== Example Cards ====
                RecommendationCard(
                    imageUrl = "https://picsum.photos/200",
                    title = "블루보틀 성수점",
                    category = "카페",
                    price = "₩₩",
                    rating = "4.5 (328)",
                    distance = "300m",
                    walk = "4분",
                    openNow = true,
                    tags = listOf("조용한", "WiFi", "디저트")
                )
            }
        }
    }
}

@Composable
fun FilterChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF6D28D9), modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 14.sp, color = Color(0xFF111827))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecommendationCard(
    imageUrl: String,
    title: String,
    category: String,
    price: String,
    rating: String,
    distance: String,
    walk: String,
    openNow: Boolean,
    tags: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "찜")
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AssistChip(category)
                        Spacer(Modifier.width(4.dp))
                        Text(price, fontSize = 12.sp, color = Color.Gray)
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("⭐ $rating", fontSize = 12.sp)
                        Text("📍 $distance", fontSize = 12.sp)
                        Text("🚶 $walk", fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(6.dp))
                    if (openNow) {
                        Text(
                            "지금 영업 중",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF16A34A),
                            modifier = Modifier
                                .background(Color(0xFFD1FAE5), RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tags.forEach {
                    Text(
                        it,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier
                            .background(Color(0xFFF3F4F6), RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AssistChip(label: String) {
    Text(
        label,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = when (label) {
            "카페" -> Color(0xFF92400E)
            "전시" -> Color(0xFFB91C1C)
            "체험" -> Color(0xFF1D4ED8)
            "관광지" -> Color(0xFF6D28D9)
            else -> Color.Black
        },
        modifier = Modifier
            .background(Color(0xFFF3F4F6), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Preview
@Composable
private fun PreviewFreeTimeRecommendationScreen() {
    RecommendationScreen(
        current = Route.Recommendations,
        onBack = {},
        onFilter = {},
        onNavigate = {}
    )
}

