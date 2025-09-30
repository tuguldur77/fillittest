/*@file:OptIn(ExperimentalMaterial3Api::class)

package com.fillit.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Schedule(
    val id: String,
    val title: String,
    val start: String,
    val end: String,
    val location: String,
    val date: String
)

@Composable
fun ScheduleViewScreen(
    onAddSchedule: () -> Unit,
    onFindRecommendations: () -> Unit,
    openSettings: () -> Unit
) {
    var selectedDate by remember { mutableStateOf("15") }
    val schedules = remember {
        listOf(
            Schedule("1", "수업", "10:00", "11:30", "건대", "2024-03-15"),
            Schedule("2", "스터디", "14:00", "16:00", "성수", "2024-03-15"),
            Schedule("3", "친구 만남", "19:30", "21:00", "홍대", "2024-03-15")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("내 일정") },
                actions = {
                    TextButton(onClick = openSettings) { Text("설정") }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddSchedule
            ) {
                Text("일정 추가")
            }

        }
    ) { inner ->
        Column(Modifier.padding(inner)) {
            // 날짜 선택 칩
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("11", "12", "13", "14", "15", "16", "17").forEach { d ->
                    AssistChip(
                        onClick = { selectedDate = d },
                        label = { Text(d) },
                        // selection handled manually
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (d == selectedDate) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
            Divider()
            LazyColumn(Modifier.fillMaxSize()) {
                items(schedules) { s ->
                    ElevatedCard(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(s.title, fontWeight = FontWeight.SemiBold)
                            Text(
                                "${s.start} - ${s.end} · ${s.location}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            Button(
                onClick = onFindRecommendations,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("빈 시간 추천 보기")
            }
        }
    }
}*/

@file:OptIn(ExperimentalMaterial3Api::class)

package com.fillit.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.fillit.app.navigation.Route
import com.fillit.app.ui.components.FillItBottomBar

@Composable
fun ScheduleViewScreen(
    current: Route,
    onNavigate: (Route) -> Unit,
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF3F4F6), Color(0xFFFFFFFF))
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "내 일정",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF111827) // text-light
                        )
                    },
                    actions = {
                        Button(
                            onClick = onAddClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B4EFF)),
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "추가", tint = Color.White)
                            Spacer(Modifier.width(4.dp))
                            Text("추가", color = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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
                // ===== Date Selector =====
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Prev", tint = Color.Gray)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DateItem("월", "11")
                        DateItem("화", "12")
                        DateItem("수", "13")
                        DateItem("목", "14")
                        DateItem("금", "15", selected = true)
                    }
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next", tint = Color.Gray)
                }

                // Progress Line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color(0xFFE5E7EB), RoundedCornerShape(50))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .height(6.dp)
                            .align(Alignment.CenterEnd)
                            .background(Color(0xFF6B4EFF), RoundedCornerShape(50))
                    )
                }

                // Schedule Cards
                ScheduleCard(
                    start = "13:00",
                    end = "14:30",
                    title = "팀플 회의",
                    location = "건대입구",
                    duration = "1시간"
                )

                EmptySlotCard("14:30 ~ 16:00 (1시간)")

                ScheduleCard(
                    start = "16:00",
                    end = "17:00",
                    title = "병원 진료",
                    location = "성수동",
                    duration = "1시간"
                )

                EmptySlotCard("17:00 ~ 19:30 (2시간)")

                ScheduleCard(
                    start = "19:30",
                    end = "21:00",
                    title = "친구 만남",
                    location = "홍대",
                    duration = "2시간"
                )
            }
        }
    }
}

@Composable
fun DateItem(day: String, date: String, selected: Boolean = false) {
    Column(
        modifier = Modifier
            .width(48.dp)
            .background(
                if (selected) Color(0xFF6B4EFF) else Color(0xFFE5E7EB),
                RoundedCornerShape(12.dp)
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            day,
            fontSize = 12.sp,
            color = if (selected) Color.White else Color.Gray
        )
        Text(
            date,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else Color(0xFF111827)
        )
    }
}

@Composable
fun ScheduleCard(start: String, end: String, title: String, location: String, duration: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(Modifier.width(60.dp)) {
                Text(start, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Text(end, fontSize = 12.sp, color = Color(0xFF6B7280))
            }
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF111827))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(location, fontSize = 12.sp, color = Color(0xFF6B7280))
                }
            }
            Box(
                modifier = Modifier
                    .background(Color(0x336B4EFF), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(duration, fontSize = 12.sp, color = Color(0xFF6B4EFF))
            }
        }
    }
}

@Composable
fun EmptySlotCard(timeRange: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFF93C5FD)), // dashed not available
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                tint = Color(0xFF3B82F6),
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(timeRange, fontSize = 12.sp, color = Color(0xFF3B82F6))
            Text("빈 시간이 있어요!", fontSize = 12.sp, color = Color(0xFF3B82F6))
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { /* search places */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B4EFF))
            ) {
                Text("이 시간에 갈만한 장소 찾기", color = Color.White)
            }
        }
    }
}


@Preview
@Composable
private fun PreviewScheduleScreen() {
    ScheduleViewScreen(
        current = Route.Schedule,
        onNavigate = {},
        onAddClick = {}
    )
}
