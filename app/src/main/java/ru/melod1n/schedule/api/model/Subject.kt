package ru.melod1n.schedule.api.model

import androidx.room.Entity

@Entity(tableName = "subjects")
data class Subject(
        var id: Int = 0,
        var colorPosition: Int = 0,
        var title: String = ""
)