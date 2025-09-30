//@file:OptIn(ExperimentalMaterial3Api::class)
//
//package com.fillit.app.ui.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//
//@Composable
//fun SettingsScreen(onBack: () -> Unit) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("설정") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "뒤로가기"
//                        )
//                    }
//                }
//            )
//        }
//    ) { inner ->
//        Column(
//            Modifier
//                .padding(inner)
//                .padding(16.dp)
//        ) {
//            Text("카테고리 선호도", style = MaterialTheme.typography.titleMedium)
//            Spacer(Modifier.height(8.dp))
//            Row {
//                FilterChip(
//                    selected = true,
//                    onClick = { },
//                    label = { Text("카페") }
//                )
//                Spacer(Modifier.width(8.dp))
//                FilterChip(
//                    selected = true,
//                    onClick = {},
//                    label = { Text("전시") }
//                )
//            }
//
//            Spacer(Modifier.height(16.dp))
//            Text("가격대", style = MaterialTheme.typography.titleMedium)
//            Row {
//                AssistChip(onClick = {}, label = { Text("무료") })
//                Spacer(Modifier.width(8.dp))
//                AssistChip(onClick = {}, label = { Text("₩₩") })
//            }
//
//            Spacer(Modifier.height(16.dp))
//            Text("알림", style = MaterialTheme.typography.titleMedium)
//            Row(
//                Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text("추천 알림 받기")
//                Switch(checked = true, onCheckedChange = {})
//            }
//
//            Spacer(Modifier.height(24.dp))
//            Button(
//                onClick = onBack,
//                modifier = Modifier.fillMaxWidth()
//            ) { Text("완료") }
//        }
//    }
//}




/*@file:OptIn(ExperimentalMaterial3Api::class)

package com.fillit.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE3E7FF), Color(0xFFF0F2FE))
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "설정",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            bottomBar = {
                BottomNavigationBar(onNavClick = onNavClick)
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .padding(inner)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==== Profile Card ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFF5B67EA), RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("김민지", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                            Text("minji.kim@gmail.com", fontSize = 14.sp, color = Color(0xFF666666))
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Google 계정",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFD97706),
                                modifier = Modifier
                                    .background(Color(0xFFFFF3E2), RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // ==== Category Section ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF5B67EA))
                        Spacer(Modifier.width(8.dp))
                        Text("관심 카테고리", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "선호하는 장소 유형을 선택하세요",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    CategoryRow("☕️ 카페", checked = true)
                    CategoryRow("🎨 전시", checked = true)
                    CategoryRow("🛠️ 체험", checked = true)
                    CategoryRow("🏞️ 관광지", checked = false)
                    CategoryRow("🍽️ 맛집", checked = false)
                    CategoryRow("🛍️ 쇼핑", checked = false)
                }

                // ==== Transport Section ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF5B67EA))
                        Spacer(Modifier.width(8.dp))
                        Text("이동 수단", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                    }
                    Spacer(Modifier.height(12.dp))
                    CategoryRow("🚶 도보", checked = true)
                    CategoryRow("🚗 자차", checked = false)
                }

                // ==== Price Section ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Menu, contentDescription = null, tint = Color(0xFF5B67EA))
                        Spacer(Modifier.width(8.dp))
                        Text("선호 가격대", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                    }
                    Spacer(Modifier.height(12.dp))
                    CategoryRow("무료", checked = true)
                    CategoryRow("₩ 저가", checked = true)
                    CategoryRow("₩₩ 보통", checked = true)
                    CategoryRow("₩₩₩ 고가", checked = false)
                }

                // ==== Notifications Section ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF5B67EA))
                        Spacer(Modifier.width(8.dp))
                        Text("알림 설정", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                    }
                    Spacer(Modifier.height(12.dp))
                    SettingRowDesc(
                        title = "장소 추천 알림",
                        subtitle = "빈 시간에 맞는 추천을 받으세요",
                        checked = true
                    )
                    SettingRowDesc(
                        title = "일정 알림",
                        subtitle = "다가오는 일정을 미리 알려드려요",
                        checked = true
                    )
                }

                // ==== Accent Permission Section (extra example) ====
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFD54F)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E2))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFD97706))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text("위치 접근 권한", fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                                Text("근처 장소 추천을 위해 위치 정보가 필요해요", fontSize = 12.sp, color = Color(0xFF666666))
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { /* handle allow */ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                            ) {
                                Text("권한 허용", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun CategoryRow(label: String, checked: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF333333))
        Switch(
            checked = checked,
            onCheckedChange = {},
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF5B67EA)
            )
        )
    }
}

@Composable
fun SettingRowDesc(title: String, subtitle: String, checked: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Medium, color = Color(0xFF333333))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF666666))
        }
        Switch(
            checked = checked,
            onCheckedChange = {},
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF5B67EA)
            )
        )
    }
}

@Composable
fun BottomNavigationBar(onNavClick: (String) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick = { onNavClick("schedule") },
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("내 일정") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onNavClick("search") },
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text("추천") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onNavClick("favorite") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text("찜목록") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { onNavClick("settings") },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("설정", fontWeight = FontWeight.Bold) }
        )
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    SettingsScreen(onBack = {}, onNavClick = {})
}
*/

@file:OptIn(ExperimentalMaterial3Api::class)

package com.fillit.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.fillit.app.ui.components.FillItBottomBar
import com.fillit.app.navigation.Route

@Composable
fun SettingsScreen(
    current: Route,
    onBack: () -> Unit,
    onNavigate: (Route) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE3E7FF), Color(0xFFF0F2FE))
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "설정",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==== Profile Card ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFF5B67EA), RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("김민지", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                            Text("minji.kim@gmail.com", fontSize = 14.sp, color = Color(0xFF666666))
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Google 계정",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFD97706),
                                modifier = Modifier
                                    .background(Color(0xFFFFF3E2), RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // ==== Category Section ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF5B67EA))
                        Spacer(Modifier.width(8.dp))
                        Text("관심 카테고리", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "선호하는 장소 유형을 선택하세요",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    CategoryRow("☕️ 카페", checked = true)
                    CategoryRow("🎨 전시", checked = true)
                    CategoryRow("🛠️ 체험", checked = true)
                    CategoryRow("🏞️ 관광지", checked = false)
                    CategoryRow("🍽️ 맛집", checked = false)
                    CategoryRow("🛍️ 쇼핑", checked = false)
                }

                // ==== Transport Section ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF5B67EA))
                        Spacer(Modifier.width(8.dp))
                        Text("이동 수단", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                    }
                    Spacer(Modifier.height(12.dp))
                    CategoryRow("🚶 도보", checked = true)
                    CategoryRow("🚗 자차", checked = false)
                }

                // ==== Price Section ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Menu, contentDescription = null, tint = Color(0xFF5B67EA))
                        Spacer(Modifier.width(8.dp))
                        Text("선호 가격대", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                    }
                    Spacer(Modifier.height(12.dp))
                    CategoryRow("무료", checked = true)
                    CategoryRow("₩ 저가", checked = true)
                    CategoryRow("₩₩ 보통", checked = true)
                    CategoryRow("₩₩₩ 고가", checked = false)
                }

                // ==== Notifications Section ====
                SettingCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF5B67EA))
                        Spacer(Modifier.width(8.dp))
                        Text("알림 설정", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF333333))
                    }
                    Spacer(Modifier.height(12.dp))
                    SettingRowDesc(
                        title = "장소 추천 알림",
                        subtitle = "빈 시간에 맞는 추천을 받으세요",
                        checked = true
                    )
                    SettingRowDesc(
                        title = "일정 알림",
                        subtitle = "다가오는 일정을 미리 알려드려요",
                        checked = true
                    )
                }
                // ==== Accent Permission Section (extra example) ====
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFD54F)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E2))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFD97706))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text("위치 접근 권한", fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                                Text("근처 장소 추천을 위해 위치 정보가 필요해요", fontSize = 12.sp, color = Color(0xFF666666))
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { /* handle allow */ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                            ) {
                                Text("권한 허용", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun CategoryRow(label: String, checked: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF333333))
        Switch(
            checked = checked,
            onCheckedChange = {},
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF5B67EA)
            )
        )
    }
}

@Composable
fun SettingRowDesc(title: String, subtitle: String, checked: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Medium, color = Color(0xFF333333))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF666666))
        }
        Switch(
            checked = checked,
            onCheckedChange = {},
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF5B67EA)
            )
        )
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    SettingsScreen(
        current = Route.Settings,
        onBack = {},
        onNavigate = {}
    )
}
