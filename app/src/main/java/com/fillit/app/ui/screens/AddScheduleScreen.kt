package com.fillit.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(onBack: () -> Unit, onSave: () -> Unit) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var start by remember { mutableStateOf(TextFieldValue("")) }
    var end by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("일정 추가") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { inner ->
        Column(Modifier
            .padding(inner)
            .padding(16.dp)) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("제목") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("시작 시간 (HH:MM)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("종료 시간 (HH:MM)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("위치") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) { Text("저장") }
        }
    }
}


@Preview
@Composable
private fun PreviewAddScheduleScreen() {
    AddScheduleScreen(onBack = {}, onSave = {})
    
}