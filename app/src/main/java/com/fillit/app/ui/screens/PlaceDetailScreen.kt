@file:OptIn(ExperimentalMaterial3Api::class)

package com.fillit.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlaceDetailScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("장소 상세") },
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
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp)
        ) {
            Text(
                "성수동 카페",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text("카페 · 320m · ₩₩")
            Spacer(Modifier.height(12.dp))
            Text("영업중 · 10:00 - 22:00", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(24.dp))
            Text("리뷰", fontWeight = FontWeight.Bold)
            Text("분위기도 좋고 커피도 맛있어요.", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
private fun PreviewPlaceDetailScreen() {
    PlaceDetailScreen(onBack = {})
}