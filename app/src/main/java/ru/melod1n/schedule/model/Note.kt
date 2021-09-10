package ru.melod1n.schedule.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
        @PrimaryKey(autoGenerate = true)
        var position: Int,
        var id: String,
        var title: String,
        var body: String,
        var color: Int,
        var subjectId: String
)