package ru.melod1n.schedule.items

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
        @PrimaryKey(autoGenerate = true)
        var position: Int,
        var id: String,
        var title: String,
        var body: String,
        var color: String,
        var subjectId: String
)