@file:OptIn(ExperimentalMaterial3Api::class)

package com.fillit.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fillit.app.BuildConfig
import com.fillit.app.ai.GenerativeClient
import com.fillit.app.navigation.Route
import com.fillit.app.ui.components.FillItBottomBar
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun RecommendationScreen(
    current: Route,
    onBack: () -> Unit,
    onFilter: () -> Unit,
    onNavigate: (Route) -> Unit
) {
    val db = Firebase.firestore
    val uid = Firebase.auth.currentUser?.uid ?: "testuser"
    val scope = rememberCoroutineScope()

    var allPlaces by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var likedPlaces by remember { mutableStateOf<List<String>>(emptyList()) }
    var preferences by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var searchQuery by remember { mutableStateOf("") }

    // ✅ Gemini connection check
    LaunchedEffect(Unit) {
        val ai = GenerativeClient(BuildConfig.GEMINI_API_KEY)
        try {
            val models = ai.listModels()
            println("✅ Gemini models available: $models")
        } catch (e: Exception) {
            println("⚠️ Gemini model list failed: ${e.message}")
        }
    }

    // ✅ Real-time preferences
    LaunchedEffect(uid) {
        db.collection("users").document(uid)
            .collection("preferences").document("preferences")
            .addSnapshotListener { doc, e ->
                if (e != null) return@addSnapshotListener
                preferences = doc?.data?.mapValues { (_, v) -> v as? Boolean ?: false } ?: emptyMap()
                println("🧩 Preferences updated: $preferences")
            }
    }

    // ✅ Real-time liked list
    LaunchedEffect(uid) {
        db.collection("users").document(uid)
            .collection("wanted")
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                likedPlaces = snapshot?.documents?.mapNotNull { it.getString("name") } ?: emptyList()
                println("💜 Liked: $likedPlaces")
            }
    }

    // ✅ Real-time listener + one-time seeding for "places"
    LaunchedEffect(Unit) {
        db.collection("places").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    val samplePlaces = listOf(
                        mapOf(
                            "name" to "블루보틀 성수점",
                            "type" to "cafe",
                            "location" to "서울 성동구 아차산로 7",
                            "price" to "₩₩",
                            "rating" to "4.7",
                            "imageUrl" to "https://picsum.photos/200?random=1",
                            "createdAt" to Timestamp.now()
                        ),
                        mapOf(
                            "name" to "성수 갤러리",
                            "type" to "exhibition",
                            "location" to "서울 성동구 연무장길 9",
                            "price" to "₩",
                            "rating" to "4.5",
                            "imageUrl" to "https://picsum.photos/200?random=2",
                            "createdAt" to Timestamp.now()
                        )
                    )
                    samplePlaces.forEach { place ->
                        db.collection("places")
                            .add(place)
                            .addOnSuccessListener { ref ->
                                println("🌱 Seeded place: ${place["name"]} → ${ref.id}")
                            }
                            .addOnFailureListener { e ->
                                println("🚨 Failed to seed ${place["name"]}: ${e.message}")
                            }
                    }
                } else {
                    println("✅ Firestore already has ${snapshot.size()} places, skipping seeding.")
                }
            }

        db.collection("places").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("⚠️ Failed to load places: ${e.message}")
                return@addSnapshotListener
            }
            val docs = snapshot?.documents?.mapNotNull { it.data } ?: emptyList()
            allPlaces = docs
            println("📍 Loaded ${docs.size} places")
        }
    }

    // ✅ Gemini keyword generator
    suspend fun generateKeywordsWithGemini(name: String): List<String> {
        return try {
            val ai = GenerativeClient(BuildConfig.GEMINI_API_KEY)
            val prompt = """
                장소 이름: "$name"
                아래 조건을 지켜서 결과를 만들어주세요.
                1. 이 장소를 묘사하는 한국어 키워드 3~5개를 생성하세요.
                2. 영어 감성 키워드도 1~2개 포함하세요 (예: cozy, chill, trendy).
                3. 쉼표(,)로 구분하고 문장은 절대 포함하지 마세요.
                4. 출력 예시: moody, espresso, seoul, cozy, minimal
            """.trimIndent()

            val rawText = ai.generate("gemini-2.5-flash", prompt)
            println("🌸 Gemini raw output: $rawText")

            rawText.split(",", "·", " ", "\n")
                .map { it.trim().lowercase() }
                .filter { it.isNotEmpty() && it.length < 20 }
                .distinct()
                .take(6)
        } catch (e: Exception) {
            println("⚠️ Gemini keyword generation failed: ${e.message}")
            emptyList()
        }
    }

    // ✅ 찜 toggle (Firestore + Gemini)
    fun toggleLike(place: Map<String, Any>) {
        val name = place["name"]?.toString() ?: return
        val location = place["location"]?.toString() ?: ""
        val ref = db.collection("users").document(uid).collection("wanted")

        ref.whereEqualTo("name", name).get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                // ❤️ Add new favorite
                scope.launch {
                    val keywords = generateKeywordsWithGemini(name)
                    val cleaned = keywords.map { it.trim().lowercase() }.distinct()
                    val data = mapOf(
                        "name" to name,
                        "location" to location,
                        "keywords" to cleaned,
                        "createdAt" to Timestamp.now()
                    )
                    ref.add(data)
                        .addOnSuccessListener {
                            println("💜 Added $name with keywords: $cleaned")
                        }
                        .addOnFailureListener {
                            println("⚠️ Failed to add favorite: ${it.message}")
                        }
                }
            } else {
                // 💔 Remove if already liked
                snapshot.documents.first().reference.delete()
                println("💔 Removed $name")
            }
        }.addOnFailureListener {
            println("🚨 toggleLike failed: ${it.message}")
        }
    }

    // ✅ Filter: show by category first, then search within that
    val filtered = remember(preferences, allPlaces, searchQuery) {
        if (allPlaces.isEmpty()) emptyList()
        else {
            // 1️⃣ Step: filter by category (관심 카테고리)
            val categoryFiltered = if (preferences.isEmpty()) {
                allPlaces
            } else {
                allPlaces.filter { place ->
                    val type = (place["type"] ?: "") as String
                    preferences[type] == true
                }
            }

            // 2️⃣ Step: apply search filter inside those
            if (searchQuery.isBlank()) {
                categoryFiltered
            } else {
                categoryFiltered.filter { place ->
                    val name = (place["name"] ?: "") as String
                    val keywords = (place["keywords"] as? List<*>)?.joinToString(",") ?: ""
                    name.contains(searchQuery, ignoreCase = true) ||
                            keywords.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }


    // ✅ UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF3F4F6), Color.White)))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column {
                    TopAppBar(
                        title = { Text("빈 시간 추천", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                    )

                    // 🔍 Search bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(Color(0xFFF2F3F5), RoundedCornerShape(24.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                        Spacer(Modifier.width(8.dp))
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("장소나 키워드 검색…", color = Color.Gray) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color(0xFF111827)
                            )
                        )
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                            }
                        }
                    }
                }
            },
            bottomBar = { FillItBottomBar(current = current, onNavigate = onNavigate) }
        ) { inner ->
            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(inner)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("검색 결과가 없습니다 😅", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(inner)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filtered.size) { index ->
                        val item = filtered[index]
                        val name = item["name"]?.toString() ?: "Unknown"
                        val type = item["type"]?.toString() ?: ""
                        val location = item["location"]?.toString() ?: ""
                        val imageUrl = item["imageUrl"]?.toString()
                            ?: "https://picsum.photos/200"
                        val price = item["price"]?.toString() ?: "₩₩"
                        val rating = item["rating"]?.toString() ?: "4.5"
                        val isFavorite = likedPlaces.contains(name)

                        RecommendationCard(
                            imageUrl = imageUrl,
                            title = name,
                            category = type,
                            price = price,
                            rating = rating,
                            location = location,
                            isFavorite = isFavorite,
                            onToggleFavorite = { toggleLike(item) } // pass full map, not just name
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(
    imageUrl: String,
    title: String,
    category: String,
    price: String,
    rating: String,
    location: String,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
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
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "찜",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("📍 $location", fontSize = 12.sp, color = Color.Gray)
                    Text("⭐ $rating • $price", fontSize = 12.sp, color = Color.Gray)
                    Text("분류: $category", fontSize = 12.sp, color = Color(0xFF6B7280))
                }
            }
        }
    }
}
