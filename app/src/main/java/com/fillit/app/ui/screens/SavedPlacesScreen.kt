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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class SavedPlace(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val imageUrl: String = "",
    val keywords: List<String> = emptyList(),
    val createdAt: Timestamp? = null
)

@Composable
fun SavedPlacesScreen(
    current: Route,
    onBack: () -> Unit,
    onFilter: () -> Unit,
    onNavigate: (Route) -> Unit
) {
    val db = Firebase.firestore
    val uid = Firebase.auth.currentUser?.uid ?: "testuser"
    var savedPlaces by remember { mutableStateOf<List<SavedPlace>>(emptyList()) }

    // ✅ Load saved "wanted" places in real-time
    LaunchedEffect(uid) {
        db.collection("users").document(uid).collection("wanted")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("⚠️ Failed to load saved places: ${e.message}")
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    SavedPlace(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        location = doc.getString("location") ?: "",
                        imageUrl = doc.getString("imageUrl") ?: "", // optional
                        keywords = (doc["keywords"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                        createdAt = doc.getTimestamp("createdAt")
                    )
                } ?: emptyList()

                savedPlaces = list
                println("💾 Loaded ${list.size} saved places")
            }
    }

    // ❤️ Remove function
    fun removeFromSaved(placeId: String) {
        db.collection("users").document(uid)
            .collection("wanted").document(placeId)
            .delete()
            .addOnSuccessListener { println("💔 Removed saved place $placeId") }
            .addOnFailureListener { println("⚠️ Failed to remove: ${it.message}") }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF3F4F6), Color.White)))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "찜한 장소",
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
                            Icon(Icons.Default.Favorite, contentDescription = "Filter")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                FillItBottomBar(current = current, onNavigate = onNavigate)
            }
        ) { inner ->
            if (savedPlaces.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(inner)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("저장된 찜이 없습니다 💔", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(inner)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(savedPlaces) { place ->
                        SavedPlaceCard(place = place, onRemove = { removeFromSaved(place.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun SavedPlaceCard(
    place: SavedPlace,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = if (place.imageUrl.isNotBlank()) place.imageUrl else "https://picsum.photos/200?blur",
                contentDescription = place.name,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(place.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = onRemove) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Remove",
                            tint = Color.Red
                        )
                    }
                }
                Text("📍 ${place.location}", fontSize = 12.sp, color = Color.Gray)
                if (place.keywords.isNotEmpty()) {
                    Text(
                        "키워드: ${place.keywords.joinToString(", ")}",
                        color = Color(0xFF6B7280),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSavedPlacesScreen() {
    SavedPlacesScreen(
        current = Route.SavedPlaces,
        onBack = {},
        onFilter = {},
        onNavigate = {}
    )
}
