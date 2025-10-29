package com.fillit.app.data

import android.util.Log
import com.fillit.app.model.Event
import com.fillit.app.model.Recurrence
import com.fillit.app.model.SessionManager
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.UUID

/**
 * Firestore의 'events' 컬렉션과 관련된 모든 데이터 작업을 처리하는 클래스(저장소).
 */
object EventRepository {

    private const val COLLECTION_NAME = "events"
    private val db = Firebase.firestore

    /**
     * 새로운 일정을 Firestore에 추가합니다.
     */
    suspend fun addEvent(event: Event): Result<String> {
        val currentUserId = SessionManager.userId
        if (currentUserId.isNullOrBlank()) {
            return Result.failure(Exception("로그인한 사용자 정보가 없습니다."))
        }

        return when (event.recurrence) {
            Recurrence.NONE -> addSingleEvent(event, currentUserId)
            Recurrence.WEEKLY -> addWeeklyRecurringEvents(event, currentUserId)
            else -> Result.failure(Exception("현재는 매주 반복만 지원합니다."))
        }
    }

    /**
     * 특정 날짜에 해당하는 모든 일정을 Firestore에서 가져옵니다.
     *
     * @param date 일정을 가져올 날짜.
     * @return 성공 시 Event 목록을 담은 Result.success, 실패 시 Exception을 담은 Result.failure를 반환합니다.
     */
    suspend fun getEventsForDate(date: LocalDate): Result<List<Event>> {
        val currentUserId = SessionManager.userId
        if (currentUserId.isNullOrBlank()) {
            return Result.failure(Exception("로그인한 사용자 정보가 없습니다."))
        }

        return try {
            // 해당 날짜의 시작(00:00:00)과 끝(23:59:59) Timestamp 계산
            val startOfDay = Timestamp(date.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond, 0)
            val endOfDay = Timestamp(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond - 1, 999_999_999)

            val querySnapshot = db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", currentUserId) // 내 일정만 필터링
                .whereGreaterThanOrEqualTo("startTime", startOfDay)
                .whereLessThanOrEqualTo("startTime", endOfDay)
                .orderBy("startTime") // 시간순으로 정렬
                .get()
                .await()

            val events = querySnapshot.toObjects<Event>()
            Log.d("EventRepository", "Fetched ${events.size} events for $date")
            Result.success(events)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error fetching events for $date", e)
            Result.failure(e)
        }
    }

    private suspend fun addSingleEvent(event: Event, userId: String): Result<String> {
        if (event.startTime != null && event.endTime != null && event.startTime.seconds >= event.endTime.seconds) {
            return Result.failure(Exception("종료 시간은 시작 시간보다 늦어야 합니다."))
        }

        return try {
            val eventToSave = event.copy(userId = userId)
            val documentReference = db.collection(COLLECTION_NAME).add(eventToSave).await()
            Log.d("EventRepository", "Single event added with ID: ${documentReference.id}")
            Result.success(documentReference.id)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error adding single event", e)
            Result.failure(e)
        }
    }

    private suspend fun addWeeklyRecurringEvents(event: Event, userId: String): Result<String> {
        val startTime = event.startTime
            ?: return Result.failure(Exception("반복 일정에는 시작 시간이 반드시 필요합니다."))

        if (event.endTime != null && startTime.seconds >= event.endTime.seconds) {
            return Result.failure(Exception("종료 시간은 시작 시간보다 늦어야 합니다."))
        }

        return try {
            val batch = db.batch()
            val groupId = UUID.randomUUID().toString()
            val baseEvent = event.copy(
                userId = userId,
                startTime = startTime, 
                recurringGroupId = groupId,
                recurrence = Recurrence.WEEKLY
            )

            val calendar = Calendar.getInstance()
            calendar.time = startTime.toDate()

            val durationSeconds = baseEvent.endTime?.let { endTime ->
                endTime.seconds - startTime.seconds
            }

            for (i in 0 until 52) { // 1년치(52주) 일정 생성
                val newStartTime = Timestamp(calendar.time)
                val newEndTime = if (durationSeconds != null) {
                    Timestamp(newStartTime.seconds + durationSeconds, 0)
                } else {
                    null
                }

                val weeklyEvent = baseEvent.copy(
                    startTime = newStartTime,
                    endTime = newEndTime
                )

                val docRef = db.collection(COLLECTION_NAME).document()
                batch.set(docRef, weeklyEvent)

                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }

            batch.commit().await()
            Log.d("EventRepository", "Weekly recurring events (52) added with group ID: $groupId")
            Result.success(groupId)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error adding weekly recurring events", e)
            Result.failure(e)
        }
    }
}
