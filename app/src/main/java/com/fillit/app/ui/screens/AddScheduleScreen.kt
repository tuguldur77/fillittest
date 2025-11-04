package com.fillit.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fillit.app.data.EventRepository
import com.fillit.app.model.Event
import com.fillit.app.model.Recurrence
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private enum class TimeField { START, END }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(onBack: () -> Unit, onSave: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var recurrence by remember { mutableStateOf(Recurrence.NONE) }
    var isLoading by remember { mutableStateOf(false) }
    var showPickerFor by remember { mutableStateOf<TimeField?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    if (showPickerFor != null) {
        val timePickerState = rememberTimePickerState(is24Hour = true)
        TimePickerDialog(
            onDismissRequest = { showPickerFor = null },
            onConfirm = {
                val time = "%02d:%02d".format(timePickerState.hour, timePickerState.minute)
                when (showPickerFor) {
                    TimeField.START -> start = time
                    TimeField.END -> end = time
                    null -> {}
                }
                showPickerFor = null
            },
            content = { TimePicker(state = timePickerState) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("일정 추가") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp)
        ) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("제목") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            TimeSelector(label = "시작 시간", time = start) { showPickerFor = TimeField.START }
            Spacer(Modifier.height(8.dp))
            TimeSelector(label = "종료 시간", time = end) { showPickerFor = TimeField.END }
            Spacer(Modifier.height(16.dp))

            RecurrenceSelector(selected = recurrence, onSelected = { recurrence = it })
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("위치 (선택)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Button(
                enabled = !isLoading,
                onClick = {
                    if (title.isBlank() || start.isBlank() || end.isBlank()) {
                        Toast.makeText(context, "제목, 시작 시간, 종료 시간을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    scope.launch {
                        val eventToSave = Event(
                            title = title,
                            location = location.ifBlank { null },
                            startTime = start.toTimestamp(),
                            endTime = end.toTimestamp(),
                            recurrence = recurrence // 선택된 반복 설정 포함
                        )

                        val result = EventRepository.addEvent(eventToSave)

                        withContext(Dispatchers.Main) {
                            result.onSuccess {
                                Toast.makeText(context, "일정이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                                onSave() // 성공 시 뒤로가기
                            }.onFailure {
                                Toast.makeText(context, "저장 실패: ${it.message}", Toast.LENGTH_LONG).show()
                            }
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("저장")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecurrenceSelector(selected: Recurrence, onSelected: (Recurrence) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }) {
        OutlinedTextField(
            value = selected.displayName,
            onValueChange = {},
            label = { Text("반복") },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            // "매일", "매월"은 비활성화 처리
            Recurrence.values().forEach { recurrence ->
                DropdownMenuItem(
                    text = { Text(recurrence.displayName) },
                    onClick = {
                        onSelected(recurrence)
                        isExpanded = false
                    },
                    enabled = recurrence == Recurrence.NONE || recurrence == Recurrence.WEEKLY
                )
            }
        }
    }
}

@Composable
private fun TimeSelector(label: String, time: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (time.isNotBlank()) "$label: $time" else label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (time.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "시간 선택",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)) {
                    content()
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismissRequest) {
                        Text("취소")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("확인")
                    }
                }
            }
        }
    }
}

private fun String.toTimestamp(): Timestamp? {
    if (this.isBlank()) return null
    return try {
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.KOREA)
        val timeDate = timeFormatter.parse(this)

        val calendar = Calendar.getInstance()
        if (timeDate != null) {
            val timeCalendar = Calendar.getInstance().apply { time = timeDate }
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
        Timestamp(calendar.time)
    } catch (e: Exception) {
        null
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAddScheduleScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        AddScheduleScreen(onBack = {}, onSave = {})
    }
}
