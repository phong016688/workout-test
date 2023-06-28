package com.example.workoutstest.utils

import com.example.workoutstest.domain.entries.DayOfWeek
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    fun getCurrentWeekDays(): List<DayOfWeek> {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        val dateFormat = SimpleDateFormat("EEE", Locale.US)
        val daysOfWeek = mutableListOf<DayOfWeek>()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        if (currentDay == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -7)
        }
        for (i in 0..6) {
            val day = DayOfWeek(
                dateFormat.format(calendar.time).uppercase(),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.time
            )
            daysOfWeek.add(day)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return daysOfWeek
    }
}