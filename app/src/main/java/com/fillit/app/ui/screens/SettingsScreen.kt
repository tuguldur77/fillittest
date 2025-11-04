@file:OptIn(ExperimentalMaterial3Api::class)

package com.fillit.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fillit.app.navigation.Route
import com.fillit.app.ui.components.FillItBottomBar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SettingsScreen(
    current: Route,
    onBack: () -> Unit,
    onNavigate: (Route) -> Unit
) {
    val db = Firebase.firestore
    val uid = Firebase.auth.currentUser?.uid ?: "testuser"

    var prefs by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    // === Real-time listener for preferences ===
    LaunchedEffect(uid) {
        db.collection("users").document(uid)
            .collection("preferences").document("preferences")
            .addSnapshotListener { doc, e ->
                if (e != null) {
                    println("⚠️ Pref load error: ${e.message}")
                    return@addSnapshotListener
                }
                if (doc != null && doc.exists()) {
                    prefs = doc.data?.mapValues { it.value as? Boolean ?: false } ?: emptyMap()
                    println("💾 Preferences updated: $prefs")
                } else {
                    println("⚠️ No preferences found for $uid")
                }
            }
    }

    // === Helper: update preference field ===
    fun updatePreference(key: String, value: Boolean) {
        val docRef = db.collection("users").document(uid)
            .collection("preferences").document("preferences")

        docRef.update(key, value)
            .addOnSuccessListener { println("✅ $key updated to $value") }
            .addOnFailureListener { e ->
                // if document doesn’t exist, create it
                docRef.set(mapOf(key to value))
                println("📄 Created new pref for $key = $value (${e.message})")
            }
    }

    // === UI Layout ===
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFFE3E7FF), Color(0xFFF0F2FE)))
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("설정", fontWeight = FontWeight.Bold, color = Color(0xFF333333)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = { FillItBottomBar(current = current, onNavigate = onNavigate) }
        ) { inner ->
            Column(
                modifier = Modifier
                    .padding(inner)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // === Category Preferences ===
                SettingCard(title = "관심 카테고리", icon = Icons.Default.Settings) {
                    CategoryRow("☕️ 카페", prefs["cafe"] == true) { updatePreference("cafe", it) }
                    CategoryRow("🎨 전시", prefs["exhibition"] == true) { updatePreference("exhibition", it) }
                    CategoryRow("🛠️ 체험", prefs["experience"] == true) { updatePreference("experience", it) }
                    CategoryRow("🏞️ 관광지", prefs["tour"] == true) { updatePreference("tour", it) }
                    CategoryRow("🍽️ 맛집", prefs["restaurant"] == true) { updatePreference("restaurant", it) }
                    CategoryRow("🛍️ 쇼핑", prefs["shopping"] == true) { updatePreference("shopping", it) }
                }

                // === Transport Mode ===
                SettingCard(title = "이동 수단", icon = Icons.Default.Settings) {
                    CategoryRow("🚶 도보", prefs["hasCar"] == false) { updatePreference("hasCar", !it) }
                    CategoryRow("🚗 자차", prefs["hasCar"] == true) { updatePreference("hasCar", it) }
                }

                // === Price Range ===
                SettingCard(title = "선호 가격대", icon = Icons.Default.Settings) {
                    CategoryRow("₩ 저가", prefs["priceLow"] == true) { updatePreference("priceLow", it) }
                    CategoryRow("₩₩ 보통", prefs["priceMid"] == true) { updatePreference("priceMid", it) }
                    CategoryRow("₩₩₩ 고가", prefs["priceHigh"] == true) { updatePreference("priceHigh", it) }
                }

                // === Notifications ===
                SettingCard(title = "알림 설정", icon = Icons.Default.Notifications) {
                    SettingRowDesc(
                        title = "추천 알림",
                        subtitle = "빈 시간에 맞는 추천을 받으세요",
                        checked = prefs["recommendationAlarm"] == true
                    ) { updatePreference("recommendationAlarm", it) }
                }
            }
        }
    }
}

// === UI Components ===
@Composable
fun SettingCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF5B67EA))
                Spacer(Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
            }
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun CategoryRow(label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF333333))
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF5B67EA)
            )
        )
    }
}

@Composable
fun SettingRowDesc(title: String, subtitle: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Medium, color = Color(0xFF333333))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF666666))
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF5B67EA)
            )
        )
    }
}