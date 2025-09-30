

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SavedPlacesScreen() {
    val scrollState = rememberScrollState()
    val categories = listOf("전체", "카페", "전시", "체험")
    var selectedCategory by remember { mutableStateOf("전체") }

    val favorites = listOf(
        FavoritePlace(
            icon = Icons.Default.LocalCafe,
            name = "블루보틀 성수점",
            category = "카페",
            price = "₩₩₩",
            rating = "4.5",
            reviews = 328,
            distance = "300m",
            tags = listOf("조용한", "WiFi"),
            savedDate = "3월 15일"
        ),
        FavoritePlace(
            icon = Icons.Default.Palette,
            name = "성수동 갤러리",
            category = "전시",
            price = "₩₩",
            rating = "4.7",
            reviews = 156,
            distance = "450m",
            tags = listOf("현대미술", "사진전"),
            savedDate = "3월 14일"
        ),
        FavoritePlace(
            icon = Icons.Default.Landscape,
            name = "한강공원 성수나들목",
            category = "관광지",
            price = "무료",
            rating = "4.6",
            reviews = 523,
            distance = "800m",
            tags = listOf("산책", "자연"),
            savedDate = "3월 13일"
        ),
        FavoritePlace(
            icon = Icons.Default.Construction,
            name = "성수동 수제화 체험관",
            category = "체험",
            price = "₩₩₩₩",
            rating = "",
            reviews = 0,
            distance = "",
            tags = emptyList(),
            savedDate = ""
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF3F4F6))
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header with title and count
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("찜한 장소", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFD1C4E9), // Purple lighten for count badge
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    "${favorites.size}개",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color(0xFF6D28D9),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category filter buttons - horizontal scroll
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                val selected = category == selectedCategory
                Button(
                    onClick = { selectedCategory = category },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) Color(0xFF6D28D9) else Color(0xFFE5E7EB),
                        contentColor = if (selected) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(category, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Favorites list
        favorites.forEach { place ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE0E0E0)
                        ) {
                            Icon(
                                imageVector = place.icon,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Text(place.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Category label
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = when (place.category) {
                                                "카페" -> Color(0xFFFAF5FF)
                                                "전시" -> Color(0xFFEDE9FE)
                                                "체험" -> Color(0xFFE0E7FF)
                                                "관광지" -> Color(0xFFF3E8FF)
                                                else -> Color.LightGray
                                            }
                                        ) {
                                            Text(
                                                place.category,
                                                color = when (place.category) {
                                                    "카페" -> Color(0xFF7C3AED)
                                                    "전시" -> Color(0xFF8B5CF6)
                                                    "체험" -> Color(0xFF6366F1)
                                                    "관광지" -> Color(0xFF7C3AED)
                                                    else -> Color.DarkGray
                                                },
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(place.price, fontSize = 14.sp, color = Color.Gray)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Rating and Location info
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.Star, contentDescription = "Star", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            place.rating,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF444444)
                                        )
                                        Spacer(Modifier.width(2.dp))
                                        Text("(${place.reviews})", fontSize = 14.sp, color = Color.Gray)
                                        Spacer(Modifier.width(12.dp))
                                        Icon(Icons.Filled.LocationOn, contentDescription = "Location", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text(place.distance, fontSize = 14.sp, color = Color.Gray)
                                    }

                                    // Tags row
                                    if (place.tags.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            place.tags.forEach { tag ->
                                                Surface(
                                                    color = Color(0xFFF3F4F6),
                                                    shape = RoundedCornerShape(50)
                                                ) {
                                                    Text(
                                                        tag,
                                                        fontSize = 12.sp,
                                                        color = Color(0xFF6B7280),
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Favorite",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(24.dp)
                                )
                            } // Row
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${place.savedDate} 저장",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Button(
                                    onClick = { /* TODO: Navigate to detail */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D28D9), contentColor = Color.White),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("보기", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class FavoritePlace(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val name: String,
    val category: String,
    val price: String,
    val rating: String,
    val reviews: Int,
    val distance: String,
    val tags: List<String>,
    val savedDate: String
)
