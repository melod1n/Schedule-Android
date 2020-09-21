package ru.melod1n.schedule.api.model

data class Day(
        var date: Long = 0,
        var dayOfWeek: Int = 0,
        var dayOfYear: Int = 0,
        var lessons: ArrayList<Lesson> = ArrayList()
)