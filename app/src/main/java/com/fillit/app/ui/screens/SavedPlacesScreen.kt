@file:OptIn(ExperimentalMaterial3Api::class)
package com.fillit.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fillit.app.navigation.Route
import com.fillit.app.ui.components.FillItBottomBar

data class SavedPlace(
    val id: String,
    val name: String,
    val category: String,
    val distance: String
)


@Composable
fun SavedPlacesScreen(
    current: Route,
    onBack: () -> Unit,
    onFilter: () -> Unit,
    onNavigate: (Route) -> Unit
) {
    val savedPlaces = listOf(
        SavedPlace("1", "성수동 카페", "카페", "320m"),
        SavedPlace("2", "성수동 갤러리", "전시", "450m")
    )

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
                            "찜목록",
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            bottomBar = {
                FillItBottomBar(current = current, onNavigate = onNavigate)
            }
        ) { inner ->
            LazyColumn(
                modifier = Modifier
                    .padding(inner)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(savedPlaces) { place ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(place.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("${place.category} · ${place.distance}", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
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
